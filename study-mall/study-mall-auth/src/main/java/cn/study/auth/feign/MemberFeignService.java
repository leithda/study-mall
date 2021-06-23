package cn.study.auth.feign;

import cn.study.auth.entity.vo.UserLoginVo;
import cn.study.auth.entity.vo.UserRegistVo;
import cn.study.common.constant.ServiceNameConstant;
import cn.study.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(ServiceNameConstant.MEMBER_SERVICE)
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);
}
