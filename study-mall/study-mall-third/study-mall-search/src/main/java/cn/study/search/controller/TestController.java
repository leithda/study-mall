package cn.study.search.controller;

import cn.study.search.entity.TestEntity;
import cn.study.search.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;


    //新增
    @PostMapping("add")
    public TestEntity testInsert(@RequestBody TestEntity entity) {
        return testService.save(entity);
    }

    //删除
    @PostMapping("delete")
    public void testDelete(@RequestBody TestEntity entity) {
        testService.delete(entity);
    }

    //查询总数
    @GetMapping("/getCount")
    public Long contextLoads() {
        return testService.count();
    }

    //查询全部列表
    @GetMapping("/getAll")
    public Iterable<TestEntity> testGetAll() {
        Iterable<TestEntity> iterable = testService.getAll();
        iterable.forEach(e->System.out.println(e.toString()));
        return iterable;
    }
}
