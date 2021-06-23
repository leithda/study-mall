



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

