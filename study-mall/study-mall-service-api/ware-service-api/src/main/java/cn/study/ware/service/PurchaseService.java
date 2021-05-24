package cn.study.ware.service;

import cn.study.ware.entity.vo.WareMergeVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.ware.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:10:48
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未领取的采购单
     * @param params 参数
     */
    PageUtils queryPageUnReceive(Map<String, Object> params);

    /**
     * 合并采购单
     * @param mergeVo  参数
     */
    void mergePurchase(WareMergeVo mergeVo);
}

