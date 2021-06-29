package cn.study.order.interceptor;

import cn.study.common.constant.AuthConstant;
import cn.study.common.to.UserInfoTo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * 用户登录拦截器
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<LinkedHashMap> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LinkedHashMap loginUser = (LinkedHashMap) request.getSession().getAttribute(AuthConstant.LOGIN_USER);
        if (Objects.nonNull(loginUser)) {
            // 用户登录
            threadLocal.set(loginUser);
            return true;
        }else{
            request.getSession().setAttribute("msg","请先登录");
            response.sendRedirect("http://auth.mall.com/login.html");
            return false;
        }
    }
}
