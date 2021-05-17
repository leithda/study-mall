package cn.study.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.study.product.entity.AttrEntity;
import cn.study.product.entity.vo.AttrGroupRelationVo;
import cn.study.product.service.AttrAttrgroupRelationService;
import cn.study.product.service.AttrService;
import cn.study.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.study.product.entity.AttrGroupEntity;
import cn.study.product.service.AttrGroupService;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.R;



/**
 * 属性分组
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;


    /**
     * 查询属性分组关联属性
     * @param attrGroupId 属性分组ID
     */
    @GetMapping("{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> attrEntityList = attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data",attrEntityList);
    }

    /**
     * 查询未被分组关联的属性信息
     * @param params 分页参数
     * @param attrGroupId 属性分组id
     */
    @GetMapping("{attrGroupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String, Object> params,
                            @PathVariable("attrGroupId") Long attrGroupId){
        PageUtils page = attrService.getAttrNoRelation(params,attrGroupId);
        return R.ok().put("page",page);
    }

    // /product/attrgroup/attr/relation
    @PostMapping("attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVoList){
        relationService.saveBatch(attrGroupRelationVoList);
        return R.ok();
    }

    /**
     * 删除属性分组关联
     * @param attrGroupRelationVoList 参数
     */
    @PostMapping("attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVoList){
        attrService.deleteRelation(attrGroupRelationVoList);
        return R.ok();
    }


    /**
     * 查看指定分类属性分组
     */
    @RequestMapping("/list/{catelogId}")
    public R listByCatelogId(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
