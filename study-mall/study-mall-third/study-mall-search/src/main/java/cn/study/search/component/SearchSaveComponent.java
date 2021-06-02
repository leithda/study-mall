package cn.study.search.component;

import cn.study.search.entity.ProductEntity;
import cn.study.search.service.es.EsProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchSaveComponent {

    @Autowired
    EsProductSaveService esProductSaveService;

    public void productStatusUp(List<ProductEntity> entityList) {
        esProductSaveService.productStatusUp(entityList);
    }

}
