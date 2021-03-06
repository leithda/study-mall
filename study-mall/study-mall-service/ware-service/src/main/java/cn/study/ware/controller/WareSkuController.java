package cn.study.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.study.common.to.SkuHasStockTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.study.ware.entity.WareSkuEntity;
import cn.study.ware.service.WareSkuService;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.R;



/**
 * 商品库存
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:10:48
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;


    @PostMapping("hasStockl")
    public R getSkusHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockTo> skuHasStockVos = wareSkuService.getSkusHasStock(skuIds);
        return R.ok().put("data",skuHasStockVos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
