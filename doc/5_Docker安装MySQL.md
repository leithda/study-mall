

1. 安装docker

   

2. 查看可用的MySQL版本

   [Docker-MySQL版本](https://hub.docker.com/_/mysql?tab=tags)

   

3. 拉取docker镜像（可以直接使用第7步操作进行拉取）

   ```bash
   docker pull mysql:{tag}
   # 如 docker pull mysql:5.7
   # docker pull mysql 会默认拉取最新版本
   ```

   

4. 查看本地是否安装了MySQL

   ```bash
   docker images
   ```

5. 创建本地数据目录及复制MySQL的配置文件

   ```bash
   mkdir -p /home/dev/data/mysql/data /home/dev/data/mysql/logs /home/dev/data/mysql/conf
   ```

   ```bash
   ├── mysql
   │   ├── conf # 拷贝自MySQL
   │   │   ├── conf.d
   │   │   │   ├── docker.cnf
   │   │   │   ├── mysql.cnf
   │   │   │   └── mysqldump.cnf
   │   │   ├── mysql.cnf
   │   │   └── mysql.conf.d
   │   │       └── mysqld.cnf
   │   ├── data
   │   └── logs
   
   ```

   

7. 启动脚本

   ```bash
   docker run -p 3306:3306 --name mysql -v /home/dev/data/mysql/conf:/etc/mysql/conf.d -v /home/dev/data/mysql/logs:/logs -v /home/dev/data/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
   ```

   

