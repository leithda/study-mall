package cn.study.search.service.impl.es;

import cn.study.common.constant.ElasticIndexConstant;
import cn.study.common.utils.R;
import cn.study.common.utils.StringUtils;
import cn.study.search.constant.PageConstant;
import cn.study.search.entity.EsProductEntity;
import cn.study.search.entity.vo.AttrResponseVo;
import cn.study.search.entity.vo.SearchParam;
import cn.study.search.entity.vo.SearchResult;
import cn.study.search.feign.ProductFeignService;
import cn.study.search.service.es.EsMallSearchService;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EsMallSearchServiceImpl implements EsMallSearchService {

    @Autowired
    RestHighLevelClient esClient;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam param) {

        SearchResult result = null;
        // 1?????????????????????
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            // 2???????????????
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            // 3?????????????????????
            result = buildSearchResult(searchResponse, param);
        } catch (IOException ignored) {

        }

        return result;
    }


    /**
     * ??????????????????
     * ?????????????????????(??????????????????????????????????????????????????????)??????????????????????????????????????????
     *
     * @param param ????????????
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /*
         * ??????????????????????????????(??????????????????????????????????????????????????????)
         */
        // 1.??????boolQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 1.1 must
        String keyword = param.getKeyword();
        if (StringUtils.isNotEmpty(keyword)) {
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("skuTitle", keyword);
            boolQuery.must(matchQuery);
        }

        // 1.2 filter
        // 1.2.2 filter - ??????????????????Id??????
        Long catelog3Id = param.getCatelog3Id();
        if (Objects.nonNull(catelog3Id)) {
            boolQuery.filter(QueryBuilders.termQuery("catelogId", catelog3Id));
        }

        // 1.2.2 ????????????id??????
        List<Long> brandIdList = param.getBrandId();
        if (CollectionUtils.isNotEmpty(brandIdList)) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", brandIdList));
        }

        // 1.2.3 ??????????????????
        List<String> attrs = param.getAttrs();
        if (CollectionUtils.isNotEmpty(attrs)) {
            // attrs=1_5???:8???&attrs=2_??????:??????
            for (String attrStr : attrs) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();

                // attrStr = 1_5???:8???
                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                // ??????????????????????????????????????????nestedQuery
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }

        }

        // 1.2.4 ????????????????????????
        Integer hasStock = param.getHasStock();
        if (Objects.nonNull(hasStock) && hasStock == 1) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", true));
        }

        // 1.2.5 ????????????????????????
        String skuPrice = param.getSkuPrice();
        if (StringUtils.isNotEmpty(skuPrice) && !"_".equals(skuPrice)) {
            // 1_500, 200_ , _2000
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] priceArr = skuPrice.split("_");
            if (skuPrice.startsWith("_")) {
                rangeQuery.lte(priceArr[1]);
            } else if (skuPrice.endsWith("_")) {
                rangeQuery.gte(priceArr[0]);
            } else {
                rangeQuery.gte(priceArr[0]);
                rangeQuery.lte(priceArr[1]);
            }
            boolQuery.filter(rangeQuery);
        }

        sourceBuilder.query(boolQuery);

        /*
         * ????????????????????????
         */
        // 2.1 ??????
        String sort = param.getSort();
        if (StringUtils.isNotEmpty(sort)) {
            // hotScore_asc/desc
            String[] s = sort.split("_");
            sourceBuilder.sort(s[0], SortOrder.fromString(s[1]));
        }

        // 2.2 ??????
        sourceBuilder.from((param.getPageNum() - 1) * PageConstant.SIZE);
        sourceBuilder.size(PageConstant.SIZE);

        // 2.3 ??????
        if (StringUtils.isNotEmpty(keyword)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /*
         * ????????????
         */
        // 3.1 ????????????
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        // 3.1.1 ?????????????????????
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        // 3.1.2 ?????????????????????
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        // 3.2 ????????????
        TermsAggregationBuilder catelog_agg = AggregationBuilders.terms("catelog_agg");
        catelog_agg.field("catelogId").size(20);
        // 3.2.1 ?????????????????????
        catelog_agg.subAggregation(AggregationBuilders.terms("catelog_name_agg").field("catelogName.keyword").size(1));
        sourceBuilder.aggregation(catelog_agg);

        // 3.3 ????????????
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(20));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);


        log.info("?????????DSL?????????{}", sourceBuilder);
        return new SearchRequest(new String[]{ElasticIndexConstant.PRODUCT_INDEX}, sourceBuilder);
    }


    /**
     * ??????????????????
     *
     * @param searchResponse ????????????
     * @param param          ??????????????????
     */
    private SearchResult buildSearchResult(SearchResponse searchResponse, SearchParam param) {
        System.out.println(searchResponse);
        SearchResult result = new SearchResult();

        // 1??? ????????????????????????
        SearchHits hits = searchResponse.getHits();
        List<EsProductEntity> EsProductEntityList = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(hits.getHits())) {
            for (SearchHit hit : hits.getHits()) {
                String jsonString = hit.getSourceAsString();
                EsProductEntity EsProductEntity = JSON.parseObject(jsonString, EsProductEntity.class);
                // ??????
                if (StringUtils.isNotEmpty(param.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String highLightTitle = skuTitle.getFragments()[0].string();
                    EsProductEntity.setSkuTitle(highLightTitle);
                }
                EsProductEntityList.add(EsProductEntity);
            }
        }
        result.setProducts(EsProductEntityList);

        Aggregations aggregations = searchResponse.getAggregations();

        // 2??? ???????????????????????????????????????
        List<SearchResult.AttrVo> attrVos = Lists.newArrayList();
        ParsedNested attrAgg = aggregations.get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();

            // ??????ID
            Long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);

            // ?????????
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);

            // ?????????
            List<String> attrValue = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets()
                    .stream()
                    .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                    .collect(Collectors.toList());
            attrVo.setAttrValue(attrValue);

            attrVos.add(attrVo);

        }

        result.setAttrs(attrVos);

        // 3??? ???????????????????????????????????????
        List<SearchResult.BrandVo> brandVos = Lists.newArrayList();
        ParsedLongTerms brandAgg = aggregations.get("brand_agg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            // ????????????ID
            Long brandId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brandId);
            // ?????????
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);

            // ????????????
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);

            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);


        // 4??? ?????????????????????????????????
        List<SearchResult.CatelogVo> catelogVos = Lists.newArrayList();
        ParsedLongTerms catelogAgg = aggregations.get("catelog_agg");
        List<? extends Terms.Bucket> catelogAggBuckets = catelogAgg.getBuckets();
        for (Terms.Bucket catelogAggBucket : catelogAggBuckets) {
            SearchResult.CatelogVo catelogVo = new SearchResult.CatelogVo();
            // ????????????ID
            String keyAsString = catelogAggBucket.getKeyAsString();
            catelogVo.setCatelogId(Long.parseLong(keyAsString));

            // ??????????????????
            ParsedStringTerms catelogNameAgg = catelogAggBucket.getAggregations().get("catelog_name_agg");
            String catelogName = catelogNameAgg.getBuckets().get(0).getKeyAsString();
            catelogVo.setCatelogName(catelogName);
            catelogVos.add(catelogVo);
        }
        result.setCatelogs(catelogVos);


        // 5??? ????????????
        // ?????????
        result.setPageNum(param.getPageNum());
        // ????????????
        result.setTotal(hits.getTotalHits().value);
        // ?????????
        int totalPages = ((int) hits.getTotalHits().value + PageConstant.SIZE - 1) / PageConstant.SIZE;
        result.setTotalPages(totalPages);

        // ??????????????????
        List<Integer> pageNavs = Lists.newArrayList();
        for (int i = Math.max(param.getPageNum() - 3, 1); i < Math.min(param.getPageNum() + 3, totalPages); i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        // 6??????????????????
        List<SearchResult.NavVo> navs = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(param.getAttrs())) {
            navs = param.getAttrs().stream().map(attr -> {
                // ?????? attrs ?????????????????????
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                if (r.getCode() == 0) {
                    LinkedHashMap data = (LinkedHashMap) r.get("attr");
                    navVo.setNavName((String) data.get("attrName"));
                } else {
                    navVo.setNavName(s[0]);
                }
                navVo.setNavValue(s[1]);

                String replace = replaceQueryString(param, attr, "attrs");
                navVo.setLink("http://search.mall.com/list.html?" + replace);

                return navVo;
            }).collect(Collectors.toList());
        }
        result.setNavs(navs);

        return result;
    }

    /**
     * ?????????????????????
     *
     * @param param ????????????
     * @param value ????????????
     * @param key   ???????????????
     */
    private String replaceQueryString(SearchParam param, String value, String key) {
        // ????????????????????????
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            encode = encode.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return param.get_queryString().replace("&" + key + "=" + encode, "");
    }
}
