package cn.study.auth.controller;

import cn.study.auth.constant.AuthConstant;
import cn.study.auth.feign.SmsFeignService;
import cn.study.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    SmsFeignService smsFeignService;

    @GetMapping("sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        String redisValue = stringRedisTemplate.opsForValue().get(AuthConstant.SMS_CODE_PREFIX + phone);
        if (redisValue != null) {
            // 是否可以再发验证码
            String redisTime = redisValue.split("_")[1];
            long rTime = Long.parseLong(redisTime);
            if (System.currentTimeMillis() - rTime < 60 * 1000) {
                // 60秒内不能再发
                return R.error(1120001, "验证码发送频率过高");
            } else {
                stringRedisTemplate.delete(AuthConstant.SMS_CODE_PREFIX + phone);
                return getCode(phone);
            }
        }
        // 防止页面刷新
        return getCode(phone);
    }

    private R getCode(@RequestParam("phone") String phone) {
        String code = UUID.randomUUID().toString().substring(0, 4);
        System.out.println("验证码:" + code);
        R r = smsFeignService.sendCode(phone,null,null,code);
        if (r.getCode() == 0) {
            code = code + "_" + System.currentTimeMillis();
            stringRedisTemplate.opsForValue().set(AuthConstant.SMS_CODE_PREFIX + phone, code, 3, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error();
        }
    }

}
