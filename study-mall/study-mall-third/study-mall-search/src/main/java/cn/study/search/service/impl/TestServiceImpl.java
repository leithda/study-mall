package cn.study.search.service.impl;

import cn.study.search.dao.TestRepository;
import cn.study.search.entity.TestEntity;
import cn.study.search.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestRepository testRepository;

    //新增
    @Override
    public TestEntity save(TestEntity user) {
        return testRepository.save(user);
    }

    //删除
    @Override
    public void delete(TestEntity user) {
        testRepository.delete(user);
        //testRepository.deleteById(user.getId());
    }

    //查询总数
    @Override
    public long count() {
        return testRepository.count();
    }

    //查询全部列表
    @Override
    public Iterable<TestEntity> getAll() {
        return testRepository.findAll();
    }
}
