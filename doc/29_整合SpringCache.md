



## 加入依赖

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```



## 配置使用redis缓存

```yaml
# Spring 配置
spring:
  cache:
    type: redis
```



## 测试使用缓存

- `@Cacheable`: Triggers cache population.触发将数据保存到缓存
- `@CacheEvict`: Triggers cache eviction.删除缓存
- `@CachePut`: Updates the cache without interfering with the method execution.不影响方法执行更新缓存
- `@Caching`: Regroups multiple cache operations to be applied on a method.组合以上多个操作
- `@CacheConfig`: Shares some common cache-related settings at class-level.在类级别共享缓存的相同配置

### 开启缓存功能 `@EnableCaching`

```java
@SpringBootApplication
@EnableCaching // 开启缓存
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.study.product.feign")
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
```



### 使用缓存

```java
@Cacheable({"category"}) // 当前方法可缓存，缓存名称空间category
@Override
public List<CategoryEntity>  getLevel1Categorys() {
    return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
}
```



## 缓存的行为

默认行为：

- 如果缓存中有，方法不再调用
- key默认自动生成： 缓存的名字：：SimpleKey\[\]
- 缓存的Value的值，默认使用JDK序列化机制，将序列化后的数据存到redis
- 默认ttl时间 -1

自定义：

- 指定生成的缓存使用的key，key属性指定，接受一个[SPEL]((https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache-spel-context))
- 指定缓存数据的存活时间，配置文件中修改ttl
- 将数据保存为Json格式



### 指定key

```java
@Cacheable(value = {"category"},key = "#root.method.name") // 当前方法可缓存，缓存名称空间category,缓存键为 level1Categorys
```



### 指定配置

```yaml
# Spring 配置
spring:
  cache:
    type: redis	# 使用Redis作为缓存
    redis:
      time-to-live: 360000 # 缓存过期时间，一小时
      key-prefix: CACHE_ # 缓存前缀
      cache-null-values: true # 缓存空值，默认true
      use-key-prefix: true # 是否开启前缀，默认true
```



## 自定义缓存配置

> 指定value序列化机制为Json序列化

```java
package cn.study.product.conf;


import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration createConfiguration(
            CacheProperties cacheProperties) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig();
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}

```

## 缓存一致性_失效模式

> 使用CacheEvict，更新缓存对应存储层数据时，删除缓存

```java
@CacheEvict(value = "category",key = "'getLevel1Categorys'")
@Override
@Transactional(rollbackFor = Exception.class)
public void updateCascade(CategoryEntity category) {
    updateById(category);

    categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
}

```



注：双写模式使用`@CachePut`注解





## Spring Cache的不足

### 读模式

- 缓存穿透：查询一个null数据，解决：缓存空数据 cache-null-values=true
- 缓存击穿：大量并发同时查询一个正好过期的数据。解决：加锁，默认无锁，`sync=true`
- 缓存雪崩：大量的key同时过期。解决：加随机时间



> 常规数据：读多写少，即时性、一致性要求不高的数据
>
> 特殊数据：特殊设计



### 写模式

- 读写加锁
- 引入canal，感知数据库的更新去更新缓存
- 读多写多，直接去数据库查询

