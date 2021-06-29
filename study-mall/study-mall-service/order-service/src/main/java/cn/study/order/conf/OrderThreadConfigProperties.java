package cn.study.order.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "order.thread")
@Component
@Data
public class OrderThreadConfigProperties {
    private Integer coreSize = 10;
    private Integer maxSize = 20;
    private Integer keepAliveTime = 10;
}
