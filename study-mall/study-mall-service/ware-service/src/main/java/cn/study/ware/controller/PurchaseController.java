package cn.study.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.study.ware.entity.vo.PurchaseDoneVo;
import cn.study.ware.entity.vo.WareMergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.study.ware.entity.PurchaseEntity;
import cn.study.ware.service.PurchaseService;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.R;



/**
 * 采购信息
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:10:48
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    /**
     * 采购单采购完成
     * @param doneVo 参数
     */
    @PostMapping("done")
    public R done(@RequestBody PurchaseDoneVo doneVo){

        purchaseService.done(doneVo);

        return R.ok();
    }

    /**
     * 领取采购单
     * @param ids 采购单id集合
     */
    @PostMapping("received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 合并采购需求到采购单
     * @param mergeVo 参数
     */
    @PostMapping("merge")
    public R merge(@RequestBody WareMergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }


    /**
     * 获取未领取采购单列表
     * @param params 参数
     */
    @RequestMapping("/unreceive/list")
    public R unReceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnReceive(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
