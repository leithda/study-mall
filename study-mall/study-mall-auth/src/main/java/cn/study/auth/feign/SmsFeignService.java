package cn.study.auth.feign;


import cn.study.common.constant.ServiceNameConstant;
import cn.study.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(ServiceNameConstant.SMS_SERVICE)
public interface SmsFeignService {
    @GetMapping("sms/sendCode")
    R sendCode(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "signName", defaultValue = "signName") String signName,
            @RequestParam(value = "templateCode", defaultValue = "templateCode") String templateCode,
            @RequestParam("code") String code);
}
