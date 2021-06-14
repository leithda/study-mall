package cn.study.search.feign;

import cn.study.common.constant.ServiceNameConstant;
import cn.study.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(ServiceNameConstant.PRODUCT_SERVICE)
public interface ProductFeignService {

    @GetMapping("product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);

}
