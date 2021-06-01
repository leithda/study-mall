package cn.study.search.service.impl;

import cn.study.search.dao.ProductRepository;
import cn.study.search.entity.ProductEntity;
import cn.study.search.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    ProductRepository repository;

    @Override
    public void productStatusUp(List<ProductEntity> entityList) {
        repository.saveAll(entityList);
    }
}
