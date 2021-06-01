package cn.study.search.dao;

import cn.study.search.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<ProductEntity, String> {
}
