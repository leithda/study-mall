



## SpringSession整合

1. 增加依赖

```xml
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
```



2. 配置Session存储类型

   ```yaml
   spring: 
     session:
       store-type: redis
   ```

   

3. 开启Session功能

```java
@EnableRedisHttpSession
public class StudyMallAuthApplication {
    // ...
}
```



4. 设置Session

```java
    @PostMapping("login")
    public String login(UserLoginVo vo, HttpSession session){

        // 远程登录
        R r = memberFeignService.login(vo);
        if(r.getCode() != 0){
            // 登录失败，返回到登录页
            return "redirect:http://auth.mall.com/login.html";
        }

        LinkedHashMap memberRespTo = (LinkedHashMap) r.get("data");

        session.setAttribute("loginUser",memberRespTo);

        // 重定向到商城首页
        return "redirect:http://mall.com";
    }
```



5. 页面获取session进行使用

```html
<li>
    <a href="http://auth.mall.com/login.html">你好，请登录
        <label class="error-label" th:if="${session.loginUser}" th:text="${session.loginUser['nickname']}"></label></a>
</li>
```

- loginUser使用map保存，通过['key']获取



## SpringSession自定义

> 相关文档：[Spring Session](https://docs.spring.io/spring-session/docs/2.5.1/reference/html5/)

```java
@Configuration
@EnableRedisHttpSession
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("mall.com");
        serializer.setCookieName("MALL_SESSION");
        return serializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

}
```

- 配置Session自定义属性，DomainName及Session名称
- 配置session序列化机制





## SpringSession核心原理

> 装饰者模式的应用

- `@EnableRedisHttpSession` 导入了 `RedisHttpSessionConfiguration`配置
  - 添加如下Bean SessionRepository >> RedisIndexedSessionRepository >> Redis操作Session，Session的增删改查

- 设置Bean SessionRepositoryFilter servlet的Filter，session过滤器，每个请求过来都会经过filter
  - 创建SessionRepositoryFilter时注入SessionRepository(RedisIndexedSessionRepository)
- Filter的核心方法

```java
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    request.setAttribute(SESSION_REPOSITORY_ATTR, this.sessionRepository);	// <1>
    SessionRepositoryFilter<S>.SessionRepositoryRequestWrapper wrappedRequest = new SessionRepositoryFilter.SessionRepositoryRequestWrapper(request, response); // <2>
    SessionRepositoryFilter.SessionRepositoryResponseWrapper wrappedResponse = new SessionRepositoryFilter.SessionRepositoryResponseWrapper(wrappedRequest, response); // <3>

    try {
        filterChain.doFilter(wrappedRequest, wrappedResponse);	// <4>
    } finally {
        wrappedRequest.commitSession();
    }

}
```

- `<1>`处，将Session的操作类设置到Session中，保证后续服务使用同一个Session的操作类进行操作
- `<2>`处，将原生的ServletRequest进行包装
- `<3>`处，包装原生的ServletResponse
- `<4>`处，将包装后的请求和响应放至到后面

- 获取session方法时，调用的是Wrapper的getSession方法

```java
HttpSession session = request.getSession();
```

- SessionRepositoryRequestWrapper的getSession方法

```java
    public SessionRepositoryFilter<S>.SessionRepositoryRequestWrapper.HttpSessionWrapper getSession(boolean create) {
        SessionRepositoryFilter<S>.SessionRepositoryRequestWrapper.HttpSessionWrapper currentSession = this.getCurrentSession();
        if (currentSession != null) {
            return currentSession;
        } else {
            S requestedSession = this.getRequestedSession();
            if (requestedSession != null) {
                if (this.getAttribute(SessionRepositoryFilter.INVALID_SESSION_ID_ATTR) == null) {
                    requestedSession.setLastAccessedTime(Instant.now());
                    this.requestedSessionIdValid = true;
                    currentSession = new SessionRepositoryFilter.SessionRepositoryRequestWrapper.HttpSessionWrapper(requestedSession, this.getServletContext());
                    currentSession.markNotNew();
                    this.setCurrentSession(currentSession);
                    return currentSession;
                }
            } else {
                if (SessionRepositoryFilter.SESSION_LOGGER.isDebugEnabled()) {
                    SessionRepositoryFilter.SESSION_LOGGER.debug("No session found by id: Caching result for getSession(false) for this HttpServletRequest.");
                }

                this.setAttribute(SessionRepositoryFilter.INVALID_SESSION_ID_ATTR, "true");
            }

            if (!create) {
                return null;
            } else {
                if (SessionRepositoryFilter.SESSION_LOGGER.isDebugEnabled()) {
                    SessionRepositoryFilter.SESSION_LOGGER.debug("A new session was created. To help you troubleshoot where the session was created we provided a StackTrace (this is not an error). You can prevent this from appearing by disabling DEBUG logging for " + SessionRepositoryFilter.SESSION_LOGGER_NAME, new RuntimeException("For debugging purposes only (not an error)"));
                }

                S session = SessionRepositoryFilter.this.sessionRepository.createSession(); // <1>
                session.setLastAccessedTime(Instant.now());
                currentSession = new SessionRepositoryFilter.SessionRepositoryRequestWrapper.HttpSessionWrapper(session, this.getServletContext());
                this.setCurrentSession(currentSession);
                return currentSession;
            }
        }
    }
```

- `<1>`处，使用sessionRepository创建Session，后续Session的所有操作委托给Redis进行
