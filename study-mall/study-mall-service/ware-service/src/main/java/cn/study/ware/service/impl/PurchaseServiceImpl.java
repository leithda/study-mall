package cn.study.ware.service.impl;

import cn.study.common.constant.WareConstant;
import cn.study.common.utils.StringUtils;
import cn.study.ware.entity.PurchaseDetailEntity;
import cn.study.ware.entity.vo.WareMergeVo;
import cn.study.ware.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;

import cn.study.ware.dao.PurchaseDao;
import cn.study.ware.entity.PurchaseEntity;
import cn.study.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {


    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper -> wrapper.eq("purchase_id", key).or().eq("sku_id", key));
        }

        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", status);
        }

        String wareId = (String) params.get("wareId");
        if (StringUtils.isNotBlank(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnReceive(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status",1)
        );

        return new PageUtils(page);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchase(WareMergeVo mergeVo) {

        Long purchaseId = mergeVo.getPurchaseId();
        if(Objects.isNull(purchaseId)){
            // 1、新建一个
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());

            this.save(purchaseEntity);

            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());

            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        updateById(purchaseEntity);
    }

}