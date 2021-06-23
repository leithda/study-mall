package cn.study.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 启动类
 */
/**
 * 1、整合MyBatis-Plus
 *      1）、导入依赖
 *      <dependency>
 *             <groupId>com.baomidou</groupId>
 *             <artifactId>mybatis-plus-boot-starter</artifactId>
 *             <version>3.2.0</version>
 *      </dependency>
 *      2）、配置
 *          1、配置数据源；
 *              1）、导入数据库的驱动。https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-versions.html
 *              2）、在application.yml配置数据源相关信息
 *          2、配置MyBatis-Plus；
 *              1）、使用@MapperScan
 *              2）、告诉MyBatis-Plus，sql映射文件位置
 *
 * 2、逻辑删除
 *  1）、配置全局的逻辑删除规则（省略）
 *  2）、配置逻辑删除的组件Bean（省略）
 *  3）、给Bean加上逻辑删除注解@TableLogic
 *
 * 3、JSR303
 *   1）、给Bean添加校验注解:javax.validation.constraints，并定义自己的message提示
 *   2)、开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应；
 *   3）、给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 *   4）、分组校验（多场景的复杂校验）
 *         1)、	@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *          给校验注解标注什么情况需要进行校验
 *         2）、@Validated({AddGroup.class})
 *         3)、默认没有指定分组的校验注解@NotBlank，在分组校验情况@Validated({AddGroup.class})下不生效，只会在@Validated生效；
 *
 *   5）、自定义校验
 *      1）、编写一个自定义的校验注解
 *      2）、编写一个自定义的校验器 ConstraintValidator
 *      3）、关联自定义的校验器和自定义的校验注解
 *      @Documented
 * @Constraint(validatedBy = { ListValueConstraintValidator.class【可以指定多个不同的校验器，适配不同类型的校验】 })
 * @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
 * @Retention(RUNTIME)
 * public @interface ListValue {
 *
 * 4、统一的异常处理
 * @ControllerAdvice
 *  1）、编写异常处理类，使用@ControllerAdvice。
 *  2）、使用@ExceptionHandler标注方法可以处理的异常。
 *
 * 5、模板引擎
 *  1）、引入thymeleaf，关闭缓存
 *  2）、静态资源放在static文件夹下，可以直接访问
 *  3）、页面放在templates文件夹下
 *
 * 6、整合Redis
 *  1）、引入data-redis-stater
 *  2）、简单配置redis的host等信息
 *  3)、使用spring boot 自动注入的 StringRedisTemplate 来操作Redis
 *
 * 7、使用Redisson作为分布式对象框架
 *  1）、引入依赖
 *  2）、配置Redisson
 *
 * 8、整合Spring Cache简化缓存开发
 *  1）、引入依赖
 *      spring-boot-starter-data-redis,spring-boot-starter-cache
 *  2)、写配置
 *      (1)、自动配置了哪些
 *          CacheAutoConfiguration 会导入 RedisCacheConfiguration
 *          自动配好了缓存管理器 RedisCacheManager
 *      (2)、配置使用 redis 作为缓存
 *          spring.cache.type=redis
 *  3)、测试使用缓存
 *      - `@Cacheable`: Triggers cache population.触发将数据保存到缓存
 *      - `@CacheEvict`: Triggers cache eviction.删除缓存
 *      - `@CachePut`: Updates the cache without interfering with the method execution.不影响方法执行更新缓存
 *      - `@Caching`: Regroups multiple cache operations to be applied on a method.组合以上多个操作
 *      - `@CacheConfig`: Shares some common cache-related settings at class-level.在类级别共享缓存的相同配置
 *      (1)、开启缓存功能 @EnableCaching
 *      (2)、使用注解完成缓存操作
 *  4)、原理
 *      CacheAutoConfiguration -> RedisCacheConfiguration -> 注入了 RedisCacheManager -> 初始化所有的缓存 -> 存在自定义配置使用自定义，否则使用默认配置
 *      -> 自定义配置只需要注入 redisCacheConfiguration 即可
 */


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.study.product.feign")
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
