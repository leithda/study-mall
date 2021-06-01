package cn.study.product.feign;

import cn.study.common.constant.ServiceNameConstant;
import cn.study.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(ServiceNameConstant.WARE_SERVICE)
public interface WareFeignService {


    @PostMapping("/ware/waresku/hasStockl")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
