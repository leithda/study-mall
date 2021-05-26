

## 下载镜像文件

```bash
docker pull elasticsearch:7.4.2
```

- [Docker Hub](https://hub.docker.com/_/elasticsearch/)



## 创建数据文件夹

```bash
mkdir -p ~/data/es/config
mkdir -p ~/data/es/data
mkdir -p ~/data/es/plugins
echo " http.host: 0.0.0.0" >> ~/data/es/config/elasticsearch.yml
```

- 注意配置文件的格式为`[空格]key:[空格]value`，忽略空格启动ES会报错：**expecting token of type [START_OBJECT] but found [VALUE_STRING]];** 

## 修改数据文件夹权限

```bash
sudo chmod -R 777 ~/data/es
```

- 不修改权限启动ES会遇到`AccessDeniedException`异常

## 启动镜像

```bash
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms128m -Xmx256m" \
-v ~/data/es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v ~/data/es/data:/usr/share/elasticsearch/data \
-v ~/data/es/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.4.2
```



## 设置ES随Docker启动自启动

```bash
docker update elasticsearch --restart=always
```



## 安装Kibana



```bash
# 获取镜像
docker pull kibana:7.4.2 --registry-mirror=https://docker.mirrors.ustc.edu.cn

# 运行
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.56.10:9200 -p 5601:5601 -d kibana:7.4.2
```

- 由于直接获取镜像速度太慢，使用`--registry-mirror`参数指定国内镜像源



## 修改kibana为中文

```bash


# 进入容器
docker exec -it kibana /bin/bash

# 确认汉化文件 x-pack\plugins\translations\translations下是否有zh-CN.json

vi config/kibana.yml


# 加入 i18n.locale: "zh-CN"

```

