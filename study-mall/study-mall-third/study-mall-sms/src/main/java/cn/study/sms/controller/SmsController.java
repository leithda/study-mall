package cn.study.sms.controller;

import cn.study.common.utils.R;
import cn.study.sms.component.SmsComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
@Slf4j
public class SmsController {

    @Autowired
    SmsComponent smsComponent;

    @GetMapping("sendCode")
    public R sendCode(@RequestParam("phoneNumber") String phoneNumber,
                      @RequestParam(value = "signName", defaultValue = "signName") String signName,
                      @RequestParam(value = "templateCode", defaultValue = "templateCode") String templateCode,
                      @RequestParam("code") String code) {

        log.info("##sendCode##{},{},{},{}", phoneNumber, signName, templateCode, code);
//        smsComponent.sendSms(phoneNumber, signName, templateCode, code);
        return R.ok();

    }

}
