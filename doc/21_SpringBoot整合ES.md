



## 加入Maven依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>
```



## 编写代码

### 实体类

```java
@Data
@Document(indexName = "test")
@ToString
public class TestEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private Integer age;

}
```



### 数据操作接口

```java
@Repository
public interface TestRepository extends ElasticsearchRepository<TestEntity, String> {

}
```



### 服务接口及实现类

```java
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
```

### 表现层控制器

```java
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
```



## 使用PostMan测试接口并使用kibana进行验证

### 调用接口

![image-20210528233849905](21_SpringBoot整合ES.assets/image-20210528233849905.png)



### kibana查询

```http
POST /test/_search
{
  "query": {
    "match_all": {}
  }
}
```

```json
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "test",
        "_type" : "_doc",
        "_id" : "1",
        "_score" : 1.0,
        "_source" : {
          "_class" : "cn.study.search.entity.TestEntity",
          "id" : "1",
          "name" : "test",
          "age" : 20
        }
      }
    ]
  }
}
```



> 其他接口类似，此章节主要完成CRUD简单操作
