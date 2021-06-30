package cn.study.order.conf;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Configuration
public class OrderFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 1、 使用 RequestContextHolder 获取请求信息
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();    // 老请求
            if (Objects.nonNull(request)) {
                // 2、同步请求头信息
                template.header("Cookie", request.getHeader("Cookie"));
            }
        };
    }
}
