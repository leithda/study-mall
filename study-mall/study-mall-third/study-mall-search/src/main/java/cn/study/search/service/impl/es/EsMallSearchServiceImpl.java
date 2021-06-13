package cn.study.search.service.impl.es;

import cn.study.common.constant.ElasticIndexConstant;
import cn.study.common.utils.StringUtils;
import cn.study.search.constant.PageConstant;
import cn.study.search.entity.EsProductEntity;
import cn.study.search.entity.vo.SearchParam;
import cn.study.search.entity.vo.SearchResult;
import cn.study.search.service.es.EsMallSearchService;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.Objects;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EsMallSearchServiceImpl implements EsMallSearchService {

    @Autowired
    RestHighLevelClient esClient;

    @Override
    public SearchResult search(SearchParam param) {

        SearchResult result = null;
        // 1、准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            // 2、执行检索
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            // 3、封装响应数据
            result = buildSearchResult(searchResponse, param);
        } catch (IOException ignored) {

        }

        return result;
    }


    /**
     * 构建检索请求
     * 模糊匹配，过滤(按照属性，分类，品牌，价格区间，库存)，排序，分页，高亮，聚合分析
     *
     * @param param 检索参数
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /*
         * 查询：模糊匹配，过滤(按照属性，分类，品牌，价格区间，库存)
         */
        // 1.构建boolQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 1.1 must
        String keyword = param.getKeyword();
        if (StringUtils.isNotEmpty(keyword)) {
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("skuTitle", keyword);
            boolQuery.must(matchQuery);
        }

        // 1.2 filter
        // 1.2.2 filter - 按照三级分类Id查询
        Long catelog3Id = param.getCatelog3Id();
        if (Objects.nonNull(catelog3Id)) {
            boolQuery.filter(QueryBuilders.termQuery("catelogId", catelog3Id));
        }

        // 1.2.2 按照品牌id查询
        List<Long> brandIdList = param.getBrandId();
        if (CollectionUtils.isNotEmpty(brandIdList)) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", brandIdList));
        }

        // 1.2.3 按照属性查询
        List<String> attrs = param.getAttrs();
        if (CollectionUtils.isNotEmpty(attrs)) {
            // attrs=1_5寸:8寸&attrs=2_黑色:白色
            for (String attrStr : attrs) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();

                // attrStr = 1_5寸:8寸
                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                // 每一个属性检索都需要生成一个nestedQuery
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }

        }

        // 1.2.4 按照是否库存查询
        Integer hasStock = param.getHasStock();
        if (Objects.nonNull(hasStock)) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", hasStock == 1));
        }

        // 1.2.5 按照价格区间查询
        String skuPrice = param.getSkuPrice();
        if (StringUtils.isNotEmpty(skuPrice)) {
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
         * 排序，分页，高亮
         */
        // 2.1 排序
        String sort = param.getSort();
        if (StringUtils.isNotEmpty(sort)) {
            // hotScore_asc/desc
            String[] s = sort.split("_");
            sourceBuilder.sort(s[0], SortOrder.fromString(s[1]));
        }

        // 2.2 分页
        sourceBuilder.from((param.getPageNum() - 1) * PageConstant.SIZE);
        sourceBuilder.size(PageConstant.SIZE);

        // 2.3 高亮
        if (StringUtils.isNotEmpty(keyword)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /*
         * 聚合分析
         */
        // 3.1 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        // 3.1.1 品牌名称子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        // 3.1.2 品牌图片子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        // 3.2 分类聚合
        TermsAggregationBuilder catelog_agg = AggregationBuilders.terms("catelog_agg");
        catelog_agg.field("catelogId").size(20);
        // 3.2.1 分类名称子聚合
        catelog_agg.subAggregation(AggregationBuilders.terms("catelog_name_agg").field("catelogName.keyword").size(1));
        sourceBuilder.aggregation(catelog_agg);

        // 3.3 属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(20));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);


        System.out.println("构建的DSL语句:\n" + sourceBuilder);
        return new SearchRequest(new String[]{ElasticIndexConstant.PRODUCT_INDEX}, sourceBuilder);
    }


    /**
     * 构建响应结果
     *
     * @param searchResponse 检索响应
     * @param param          页面请求参数
     */
    private SearchResult buildSearchResult(SearchResponse searchResponse, SearchParam param) {
        System.out.println(searchResponse);
        SearchResult result = new SearchResult();

        // 1、 所有查询到的商品
        SearchHits hits = searchResponse.getHits();
        List<EsProductEntity> EsProductEntityList = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(hits.getHits())) {
            for (SearchHit hit : hits.getHits()) {
                String jsonString = hit.getSourceAsString();
                EsProductEntity EsProductEntity = JSON.parseObject(jsonString, EsProductEntity.class);
                // 高亮
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

        // 2、 当前所有商品涉及的所有属性
        List<SearchResult.AttrVo> attrVos = Lists.newArrayList();
        ParsedNested attrAgg = aggregations.get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();

            // 属性ID
            Long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);

            // 属性名
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);

            // 属性值
            List<String> attrValue = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets()
                    .stream()
                    .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                    .collect(Collectors.toList());
            attrVo.setAttrValue(attrValue);

            attrVos.add(attrVo);

        }

        result.setAttrs(attrVos);

        // 3、 当前所有商品涉及的品牌信息
        List<SearchResult.BrandVo> brandVos = Lists.newArrayList();
        ParsedLongTerms brandAgg = aggregations.get("brand_agg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            // 获取品牌ID
            Long brandId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brandId);
            // 品牌名
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);

            // 品牌图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);

            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);


        // 4、 当前商品涉及的分类信息
        List<SearchResult.CatelogVo> catelogVos = Lists.newArrayList();
        ParsedLongTerms catelogAgg = aggregations.get("catelog_agg");
        List<? extends Terms.Bucket> catelogAggBuckets = catelogAgg.getBuckets();
        for (Terms.Bucket catelogAggBucket : catelogAggBuckets) {
            SearchResult.CatelogVo catelogVo = new SearchResult.CatelogVo();
            // 获取分类ID
            String keyAsString = catelogAggBucket.getKeyAsString();
            catelogVo.setCatelogId(Long.parseLong(keyAsString));

            // 获取分类名称
            ParsedStringTerms catelogNameAgg = catelogAggBucket.getAggregations().get("catelog_name_agg");
            String catelogName = catelogNameAgg.getBuckets().get(0).getKeyAsString();
            catelogVo.setCatelogName(catelogName);
            catelogVos.add(catelogVo);
        }
        result.setCatelogs(catelogVos);


        // 5、 分页信息
        // 当前页
        result.setPageNum(param.getPageNum());
        // 总记录数
        result.setTotal(hits.getTotalHits().value);
        // 总页数
        int totalPages = ((int) hits.getTotalHits().value + PageConstant.SIZE - 1) / PageConstant.SIZE;
        result.setTotalPages(totalPages);
        // 导航页码处理

        List<Integer> pageNavs = Lists.newArrayList();
        for (int i = Math.max(param.getPageNum() - 3, 1); i < Math.min(param.getPageNum() + 3, totalPages); i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        return result;
    }
}
