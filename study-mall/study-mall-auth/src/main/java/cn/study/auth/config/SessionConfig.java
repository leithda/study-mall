package cn.study.auth.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * SpringSession核心原理
 * 1）、@EnableRedisHttpSession 导入了 RedisHttpSessionConfiguration配置
 *      1）、添加如下Bean SessionRepository >> RedisIndexedSessionRepository >> Redis操作Session，Session的增删改查
 * 继承 SpringHttpSessionConfiguration
 *      1）、设置Bean SessionRepositoryFilter servlet的Filter，session过滤器，每个请求过来都会经过filter
 *          创建SessionRepositoryFilter时注入SessionRepository(RedisIndexedSessionRepository)
 *      2）、原生的request和response被包装为SessionRepositoryRequestWrapper、SessionRepositoryResponseWrapper
 *      3）、后续获取session时，getSession调用的是Wrapper的getSession =》 从SessionRepository 中获取到
 */

@Configuration
@EnableRedisHttpSession
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("mall.com");
        serializer.setCookieName("MALL_SESSION");
        return serializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

}
