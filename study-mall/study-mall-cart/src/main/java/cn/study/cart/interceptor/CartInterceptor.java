package cn.study.cart.interceptor;

import cn.study.cart.vo.UserInfoTo;
import cn.study.common.constant.AuthConstant;
import cn.study.common.constant.CartConstant;
import cn.study.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * 购物车拦截器
 * 在执行目标方法前，判断用户的登录状态，并封装传递给controller目标请求
 */

public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    /**
     * 目标方法执行前进行拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        LinkedHashMap loginUser = (LinkedHashMap) request.getSession().getAttribute(AuthConstant.LOGIN_USER);
        if (Objects.nonNull(loginUser)) {
            // 用户登录
            userInfoTo.setUserId(Long.parseLong("" + loginUser.get("id")));
        }
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (CartConstant.TEMP_USER_COOKIE_NAME.equals(name)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }

        // 没有临时用户令牌，分配一个随机key
        if (StringUtils.isEmpty(userInfoTo.getUserKey())) {
            userInfoTo.setUserKey(UUID.randomUUID().toString());
        }

        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 业务执行后，分配临时用户让浏览器保存
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 浏览器保存cookie
        UserInfoTo userInfoTo = threadLocal.get();
        if (!userInfoTo.isTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("mall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_MAX_AGE);
            response.addCookie(cookie);
        }
    }
}
