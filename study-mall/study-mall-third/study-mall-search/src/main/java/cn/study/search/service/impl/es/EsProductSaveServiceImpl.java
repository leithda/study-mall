package cn.study.search.service.impl.es;

import cn.study.search.dao.es.EsProductRepository;
import cn.study.search.entity.ProductEntity;
import cn.study.search.service.es.EsProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class EsProductSaveServiceImpl implements EsProductSaveService {

    @Autowired
    EsProductRepository repository;

    @Override
    public void productStatusUp(List<ProductEntity> entityList) {
        repository.saveAll(entityList);
    }
}
