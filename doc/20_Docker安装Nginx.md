

1. 创建Nginx数据目录

   ```bash
   mkdir -p ~/data/nginx/conf ~/data/nginx/html ~/data/nginx/logs
   ```

   

2. 随便启动一个Nginx实例

```bash
docker run -p 80:80 --name nginx -d nginx
```



3. 拷贝配置文件

```bash
cd ~/data/nginx
docker cp nginx:/etc/nginx conf
```



4. 停止之前的容器并删除

   ```bash
   docker stop nginx
   docker rm nginx
   ```

5. 启动新容器

   ```bash
   docker run -p 80:80 --name nginx \
   -v ~/data/nginx/html:/usr/share/nginx/html \
   -v ~/data/nginx/logs:/var/log/nginx \
   -v ~/data/nginx/conf:/etc/nginx \
   -d nginx
   ```

6. 测试

   ```bash
   echo "<h2>Hello Nginx</h2>" > ~/data/nginx/html/index.html
   ```

7. 访问`http://192.168.56.10`

8. 设置自动启动

   ```bash
   docker update nginx --restart=always
   # 可以在启动(docker run)时加入 --restart 使容器随docker启动而启动
   ```

   