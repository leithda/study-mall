package cn.study.sms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("sms")
public class NacosController {

    @Value("${nacos.conf.version:0.0}")
    private String version;


    @GetMapping("version")
    public String getVersion() {
        return version;
    }
}
