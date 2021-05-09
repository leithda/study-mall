package cn.study.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MinioServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinioServiceApplication.class, args);
    }
}
