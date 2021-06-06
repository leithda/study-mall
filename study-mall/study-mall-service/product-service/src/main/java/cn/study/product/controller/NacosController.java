package cn.study.product.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 Nacos 配置中心
 */
@RestController
@RequestMapping("product/nacos")
@RefreshScope
public class NacosController {

    @Value("${nacos.conf.version:0.0}")
    private String version;


    @GetMapping("version")
    public String getVersion() {
        return version;
    }
}
