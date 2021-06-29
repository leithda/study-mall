package cn.study.order.feign;

import cn.study.common.constant.ServiceNameConstant;
import cn.study.order.entity.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(ServiceNameConstant.MEMBER_SERVICE)
public interface MemberFeignService {

    @GetMapping("member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
