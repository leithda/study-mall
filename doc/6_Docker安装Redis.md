



1. 拉取docker镜像

   ```bash
   docker pull redis
   ```

   

2. 创建配置文件夹

   ```bash
   /home/dev/data/redis/data  /home/dev/data/redis/conf
   
   ```

3. 创建配置文件

   ```bash
   touch redis.conf
   ```

   ```bash
   └── redis
       ├── data
       │   ├── appendonly.aof
       │   └── dump.rdb
       └── redis.conf # 拷贝自Redis的配置文件
   
   ```

4. 启动

   ```bash
   docker run -p 6379:6379 --name redis -v /home/dev/data/redis/redis.conf:/etc/redis/redis.conf  -v /home/dev/data/redis/data:/data -d redis redis-server /etc/redis/redis.conf --appendonly yes
   ```

   

