package cn.study.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "cart.thread")
@Component
@Data
public class CartThreadConfigProperties {
    private Integer coreSize = 10;
    private Integer maxSize = 20;
    private Integer keepAliveTime = 10;
}
