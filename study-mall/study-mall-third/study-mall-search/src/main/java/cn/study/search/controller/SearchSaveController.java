package cn.study.search.controller;

import cn.study.common.utils.R;
import cn.study.search.component.SearchSaveComponent;
import cn.study.search.entity.EsProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ES 保存
 */
@RequestMapping("search/save")
@RestController
@Slf4j
public class SearchSaveController {


    @Autowired
    SearchSaveComponent searchSaveComponent;
    /**
     * 保存商品信息
     * @param entityList 商品信息
     */
    @PostMapping("product")
    public R productStatusUp(@RequestBody List<EsProductEntity> entityList){
        try {
            searchSaveComponent.productStatusUp(entityList);
        }catch (Exception e){
            log.error("保存商品信息报错:",e);
            return R.error();
        }
        return R.ok();
    }
}
