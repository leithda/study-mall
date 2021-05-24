package cn.study.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.study.ware.feign"})
public class WareServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WareServiceApplication.class, args);
    }
}
