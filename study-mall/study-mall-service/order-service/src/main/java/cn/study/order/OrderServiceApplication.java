package cn.study.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 启动类
 * 使用RabbitMQ
 * 1. 引入RabbitMQ依赖
 * 2. 给容器中自动配置了
 *  RabbitTemplate 、AmqpAdmin 、CachingConnectionFactory、 RabbitMessagingTemplate
 * 3. 通过以下类进行配置
 *      @ConfigurationProperties(prefix = "spring.rabbitmq")
 *      public class RabbitProperties {}
 * 4. @EnableRabbit 开启RabbitMQ功能
 * 5. 监听消息使用 @RabbitListener: 需要先行开启@EnableRabbit
 * 6. @RabbitHandler , 当队列中存在不同的消息载体时，可以使用@RabbitHandler重载进行处理。
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
@EnableRabbit
@EnableFeignClients
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
