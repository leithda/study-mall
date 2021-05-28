package cn.study.search.dao;

import cn.study.search.entity.TestEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends ElasticsearchRepository<TestEntity, String> {

}
