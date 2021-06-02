

Thymeleaf官方文档:

[Thymeleaf](https://www.thymeleaf.org/)

---



## 引入依赖

```xml
        <!-- Thymeleaf 模板引擎 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!-- 热部署 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
```

- 引入`devtools`后，修改页面使用快捷键`ctrl+shift+f9`编译当前文件即可在浏览器中看到效果，**否则需要重启服务器**。

## 复制资源

将首页静态资源复制到项目`resource/static`目录下，将页面资源复制到`resource/templates`目录下。导入后的`product-service`模块目录如下：

```tex
├─src
│  ├─main
│  │  ├─java    
│  │  └─resources
│  │      ├─static
│  │      │  └─index
│  │      │      ├─css
│  │      │      ├─img
│  │      │      ├─js
│  │      │      └─json
│  │      │              catalog.json
│  │      └─templates
└  │          └─ index.html   
```

- 这里省略不必要的目录，只展示导入后有变更的目录`static、templates`

## 修改配置

在`application-dev.yml`中加入thymeleaf缓存配置，开发期间关闭缓存功能.

```yaml
spring:
  thymeleaf:
    cache: false
```



## 测试静态资源

访问`product-service`对应的ip及端口，如`http://localhost:9040/`



## 新增表现层控制器

> 新增web包用来存放web层控制器

```java
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities =  categoryService.getLevel1Categorys();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }
}
```



## 修改页面

找到index.html如下位置

```html
    <!--轮播主体内容-->
    <div class="header_main">
      <div class="header_banner">
        <div class="header_main_left">
          <ul>
            <li th:each="category : ${categorys}">
              <a href="#" class="header_main_left_a" th:attr="ctg-data=${category.catId}"><b th:text="${category.name}">家用电器</b></a>
            </li>
          </ul>
            <!-- //... 省略 -->
```

- 将硬编码的数据更改为后端传过来的值，使用`th:each`进行遍历，并通过`${}`进行使用。
- ctrl+shift+F9，编译index.html，浏览器刷新，查看最新效果。



> 后续业务代码开发不再详细介绍，请查看具体视频~