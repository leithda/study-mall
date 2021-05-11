

1. 安装docker

   

2. 查看可用的MySQL版本

   [Docker-MySQL版本](https://hub.docker.com/_/mysql?tab=tags)

   

3. 拉取docker镜像（可以直接使用第7步操作进行拉取）

   ```bash
   docker pull mysql:5.7
   # docker pull mysql:{tag}
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
   │   │   ├── docker.cnf
   │   │   ├── mysql.cnf
   │   │   └── mysqldump.cnf
   │   ├── data
   │   └── logs
   
   ```

   - 配置文件挂载到服务器主要为了修改方便，**配置文件可以通过如下方式获取**.

   ```bash
   # 启动获取配置文件的docker容器
   $ docker run -p 23306:3306 --name mysql_config -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
   
   # 查看容器是否启动
   $ docker ps
   CONTAINER ID   IMAGE       COMMAND                  CREATED          STATUS          PORTS                                                    NAMES
   e454f36eb8c5   mysql:5.7   "docker-entrypoint.s…"   26 seconds ago   Up 24 seconds   33060/tcp, 0.0.0.0:23306->3306/tcp, :::23306->3306/tcp   mysql_config
   
   # 进入容器，查看配置文件
   $ docker exec -it mysql_config /bin/bash
   $ cd /etc/mysql/conf.d/
   $ ls -rlt
   total 12
   -rw-r--r-- 1 root root 55 Aug  3  2016 mysqldump.cnf
   -rw-r--r-- 1 root root  8 Aug  3  2016 mysql.cnf
   -rw-r--r-- 1 root root 43 Apr 19 18:57 docker.cnf
   
   $ exit # 退出容器
   
   # 拷贝容器中文件到本机， `.` 表示拷贝到当前目录
   $ docker cp mysql_config:/etc/mysql/conf.d/ .
   
   # 查看拷贝结果
   $ ls
   conf.d  data  front  install_package  java  middle
   
   # 将conf.d中文件拷贝到指定的文件夹即可
   $ mv conf.d/* ~/data/mysql/conf
   
   # 关闭容器并删除
   $ docker stop mysql_config
   $ docker rm mysql_config
   
   ```

   

6. 启动脚本

   ```bash
   docker run -p 3306:3306 --name mysql -v /home/dev/data/mysql/conf:/etc/mysql/conf.d -v /home/dev/data/mysql/logs:/logs -v /home/dev/data/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
   ```

   

