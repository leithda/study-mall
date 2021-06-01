package cn.study.product.service.impl;

import cn.study.common.constant.ProductConstant;
import cn.study.common.to.SkuHasStockTo;
import cn.study.common.to.SkuReductionTo;
import cn.study.common.to.SpuBoundsTo;
import cn.study.common.to.es.SkuEsModel;
import cn.study.common.utils.R;
import cn.study.product.entity.*;
import cn.study.product.entity.vo.*;
import cn.study.product.feign.CouponFeignService;
import cn.study.product.feign.SearchFeignService;
import cn.study.product.feign.WareFeignService;
import cn.study.product.service.*;
import org.apache.commons.collections4.CollectionUtils;
import cn.study.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;

import cn.study.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuSaveVo vo) {
        // 1、保存基本信息 ,pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        // 2、保存spu的描述图片, pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity infoDescEntity = new SpuInfoDescEntity();
        infoDescEntity.setSpuId(spuInfoEntity.getId());
        infoDescEntity.setDecript(String.join(",", decript));
        this.saveSpuInfoDesc(infoDescEntity);

        // 3、保存spu的图片集, pms_spu_info_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);

        // 4、保存spu的规格参数, pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntityList = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());

        productAttrValueService.saveProductAttr(productAttrValueEntityList);

        // 5、保存spu的积分信息，study_mall_sms --> sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundsTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }


        // 6、保存spu对应的sku信息
        List<Skus> skus = vo.getSkus();
        if (CollectionUtils.isNotEmpty(skus)) {
            skus.forEach(sku -> {
                String defaultImage = "";
                for (Images img : sku.getImages()) {
                    if (img.getDefaultImg() == 1) {
                        defaultImage = img.getImgUrl();
                    }
                }

                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);


                skuInfoEntity.setSkuDefaultImg(defaultImage);
                // 6.1、sku的基本信息, pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> skuImagesEntityList = sku.getImages().stream()
                        .map(img -> {
                            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                            skuImagesEntity.setSkuId(skuId);
                            skuImagesEntity.setImgUrl(img.getImgUrl());
                            skuImagesEntity.setDefaultImg(img.getDefaultImg());
                            return skuImagesEntity;
                        })
                        .filter(skuImagesEntity -> StringUtils.isNotBlank(skuImagesEntity.getImgUrl()))
                        .collect(Collectors.toList());
                // 6.2、sku的图片信息, pms_sku_images
                skuImagesService.saveBatch(skuImagesEntityList);

                // 6.3、sku的销售属性信息, pms_sku_sale_attr_value
                List<Attr> attrList = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = attrList.stream().map(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

                // 6.4、sku的优惠、满减信息, study_mall_sms --> sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }


    }

    /**
     * 保存 spu 基本信息
     *
     * @param spuInfoEntity spu基本信息
     */
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        baseMapper.insert(spuInfoEntity);
    }

    @Override
    public void saveSpuInfoDesc(SpuInfoDescEntity infoDescEntity) {
        spuInfoDescService.save(infoDescEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();


        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(w -> w.eq("id", key).or().like("spu_name", key));
        }

        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotBlank(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catelog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {


        // 组装需要的数据
        // 1、查出当前SpuId对应的所有sku信息
        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skuInfoEntityList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        // 查询所有的规格参数
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        Set<Long> attrIdSet = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrs = baseAttrs.stream()
                .filter(baseAttr -> attrIdSet.contains(baseAttr.getAttrId()))
                .map(baseAttr ->{
                    SkuEsModel.Attrs attr = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(baseAttr,attr);
                    return attr;
                })
                .collect(Collectors.toList());

        // 查询库存信息
        Map<Long, Boolean> skuId2hasStockMap = null;
        try {
            R skusHasStock = wareFeignService.getSkusHasStock(skuIds);
            List<SkuHasStockTo> skuHasStockTos = (List<SkuHasStockTo>) skusHasStock.get("data");
            skuId2hasStockMap = skuHasStockTos.stream().collect(Collectors.toMap(SkuHasStockTo::getSkuId, SkuHasStockTo::getHasStock));

        }catch (Exception e){
            log.error("库存查询服务异常，原因:{}",e);
        }
        // 2、封装每个sku信息
        Map<Long, Boolean> finalSkuId2hasStockMap = skuId2hasStockMap;
        List<SkuEsModel> skuEsModels = skuInfoEntityList.stream().map(sku -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,esModel);
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            if(Objects.isNull(finalSkuId2hasStockMap)){
                esModel.setHasStock(true);
            }else {
                esModel.setHasStock(finalSkuId2hasStockMap.get(sku.getSkuId()));
            }
            // TODO: 热度评分，默认为0
            esModel.setHotScore(0L);

            BrandEntity brand = brandService.getById(sku.getBrandId());
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());

            CategoryEntity category = categoryService.getById(sku.getCatalogId());
            esModel.setCatelogName(category.getName());
            // 设置规格属性
            esModel.setAttrs(attrs);
            return esModel;
        }).collect(Collectors.toList());

        // TODO 将商品发送给ES进行保存。
        R r = searchFeignService.productStatusUp(skuEsModels);
        if(r.getCode() == 0){
            // 修改商品上架状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else{
            // TODO 接口幂等性问题
            /**
             * Feign 的调用流程
             * 1、 构造请求数据，将请求数据转为Json
             * 2、 发送请求执行
             */
        }
    }

}