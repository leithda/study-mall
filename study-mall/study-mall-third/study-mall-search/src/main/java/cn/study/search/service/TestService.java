package cn.study.search.service;

import cn.study.search.entity.TestEntity;

public interface TestService {

    /**
     * 统计数量
     */
    long count();

    /**
     * 保存
     * @param testEntity 实体类
     */
    TestEntity save(TestEntity testEntity);

    /**
     * 删除
     * @param testEntity 实体类
     */
    void delete(TestEntity testEntity);

    /**
     * 列表
     */
    Iterable<TestEntity> getAll();

}
