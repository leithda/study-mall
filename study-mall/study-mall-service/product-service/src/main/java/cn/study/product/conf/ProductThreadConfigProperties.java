package cn.study.product.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "product.thread")
@Component
@Data
public class ProductThreadConfigProperties {
    private Integer coreSize = 10;
    private Integer maxSize = 20;
    private Integer keepAliveTime = 10;
}
