package cn.study.order.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
//@EnableConfigurationProperties(ProductThreadConfigProperties.class)   // 使用Component注入，无需在此处再次配置
public class OrderThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(OrderThreadConfigProperties properties){
        return new ThreadPoolExecutor(
                properties.getCoreSize(),
                properties.getCoreSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
