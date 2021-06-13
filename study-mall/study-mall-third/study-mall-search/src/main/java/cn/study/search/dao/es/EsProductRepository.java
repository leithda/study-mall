package cn.study.search.dao.es;

import cn.study.search.entity.EsProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsProductRepository extends ElasticsearchRepository<EsProductEntity, String> {
}
