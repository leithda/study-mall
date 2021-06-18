package cn.study.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StudyMallAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyMallAuthApplication.class, args);
    }

}