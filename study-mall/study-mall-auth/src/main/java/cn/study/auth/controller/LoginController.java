package cn.study.auth.controller;

import cn.study.auth.entity.vo.UserLoginVo;
import cn.study.auth.entity.vo.UserRegistVo;
import cn.study.auth.feign.MemberFeignService;
import cn.study.auth.feign.SmsFeignService;
import cn.study.common.constant.AuthConstant;
import cn.study.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    SmsFeignService smsFeignService;

    @Autowired
    MemberFeignService memberFeignService;

    @GetMapping("sms/sendCode")
    @ResponseBody
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

    @GetMapping("login.html")
    public String loginPage(HttpSession httpSession){
        Object attribute = httpSession.getAttribute(AuthConstant.LOGIN_USER);
        if(Objects.isNull(attribute)){
            return "login";
        }else{
            return "redirect:http://mall.com";
        }
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

    /**
     * 注册成功跳至登录页
     */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo userRegistVo, BindingResult result,
            /* 模拟重定向携带数据*/RedirectAttributes redirectAttributes) {
        Map<String, String> map = new HashMap<>(16);
        // 验证码校验
        String redisValue = stringRedisTemplate.opsForValue().get(AuthConstant.SMS_CODE_PREFIX + userRegistVo.getPhone());
        if (redisValue != null) {
            String redisCode = redisValue.split("_")[0];
            if (!redisCode.equalsIgnoreCase(userRegistVo.getCode())) {
                map.put("msg", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", map);
                return "redirect:http://auth.mall.com/reg.html";
            }
            // 验证通过删除验证码
            stringRedisTemplate.delete(AuthConstant.SMS_CODE_PREFIX + userRegistVo.getPhone());
        }
        if (result.hasErrors()) {
            //效验出错
            Map<String, String> error = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            BeanUtils.copyProperties(error, map);
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://auth.mall.com/reg.html";
        }
        // 注册,调用远程保存 mall-member
        R r = memberFeignService.regist(userRegistVo);
        if (r.getCode() != 0) {
            map.put("msg",(String)r.get("msg"));
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://auth.mall.com/reg.html";
        }
        // 注册成功，返回登录页
        return "redirect:http://auth.mall.com/login.html";
    }


    @PostMapping("login")
    public String login(UserLoginVo vo, HttpSession session){

        // 远程登录
        R r = memberFeignService.login(vo);
        if(r.getCode() != 0){
            // 登录失败，返回到登录页
            return "redirect:http://auth.mall.com/login.html";
        }

        LinkedHashMap memberRespTo = (LinkedHashMap) r.get("data");
        session.setAttribute(AuthConstant.LOGIN_USER,memberRespTo);

        // 重定向到商城首页
        return "redirect:http://mall.com";
    }

}
