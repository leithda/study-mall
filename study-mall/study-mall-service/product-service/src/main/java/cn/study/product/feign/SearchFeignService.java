package cn.study.product.feign;


import cn.study.common.constant.ServiceNameConstant;
import cn.study.common.to.es.SkuEsModel;
import cn.study.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(ServiceNameConstant.SEARCH_SERVICE)
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> entityList);
}
