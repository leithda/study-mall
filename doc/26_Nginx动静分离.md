

## Nginx动静分离

1. 将静态资源放到`nginx/html/static`目录下。修改页面内引用。
2. 修改Nginx配置，静态资源转发到本地路径

```nginx
server {
    listen       80;
    listen  [::]:80;
    server_name  mall.com;

    location /static/ {
        root   /usr/share/nginx/html;
    }

    location / {
        proxy_set_header Host $host; # 转发时携带Host请求头
        proxy_pass http://mall;
        
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }

}

```

- 配置`/static/`转发到本地路径



## 动静分离压测

 

## 三级分类获取优化

多次查询数据库改为一次查询，根据父分类获取子分类方法抽取为公共方法，优化后代码如下：

```java
@Override
public Map<String, List<Catelog2Vo>> getCatelogJson() {

    List<CategoryEntity> allCategoryEntityList = baseMapper.selectList(null);

    // 查出所有1级分类
    List<CategoryEntity> level1Categorys = getCatergoryListByPid(allCategoryEntityList,0L);

    // 封装数据
    return level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
        // 每一个1级节点，查询2级分类
        List<CategoryEntity> level2Categorys = getCatergoryListByPid(allCategoryEntityList,v.getCatId());

        // 封装2级分类
        List<Catelog2Vo> catelog2Vos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(level2Categorys)) {
            catelog2Vos = level2Categorys.stream().map(l2 -> {
                Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), l2.getName(), l2.getCatId().toString(), null);
                // 查询3级分类
                List<CategoryEntity> level3Catelogs = getCatergoryListByPid(allCategoryEntityList,l2.getCatId());
                if(CollectionUtils.isNotEmpty(level3Catelogs)){
                    List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catelogs
                            .stream()
                            .map(l3 -> new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName()))
                            .collect(Collectors.toList());
                    catelog2Vo.setCatalog3List(catelog3Vos);
                }
                return catelog2Vo;
            }).collect(Collectors.toList());
        }
        return catelog2Vos;
    }));
}

/**
 * 根据父ID获取子分类列表
 * @param allCategoryEntityList 全量分类数据
 * @param parentCid 父分类ID
 */
private List<CategoryEntity> getCatergoryListByPid(List<CategoryEntity> allCategoryEntityList, Long parentCid) {
    return allCategoryEntityList.stream().filter(item->item.getParentCid().equals(parentCid)).collect(Collectors.toList());
}
```



- **压测后，吞吐量从30多变为369**，具体结果请参考 [25_性能压测](./25_性能压测.md#测试中间件性能) 

