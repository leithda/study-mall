

1. 安装 `yum-utils`工具包

   ```bash
   yum install -y yum-utils 
   ```

   

2. 添加docker的yum源(阿里源)

   ```bash
   yum-config-manager \
       --add-repo \
       http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
   ```

   

3. 卸载旧版本

   ```bash
   yum -y remove docker docker-common docker-selinux docker-engine
   ```

   

4. 查看所有版本，并选择指定版本安装

   ```bash
   yum list docker-ce --showduplicates | sort -r
   ```

   

5. 安装docker

   ```bash
   $ yum install docker-ce (这样写默认安装最新版本)
   $ yum install  docker-ce-<VERSION_STRING> (指定安装版本) 
   例： yum install docker-ce-18.03.1.ce
   ```

   

6. 启动并加入开机启动

   ```bash
   $ systemctl start docker       (重启命令  $  systemctl restart docker ) 
   $ systemctl enable docker   开机启动
   $ docker version  查看docker版本号
   ```

   

7. 验证安装成功

   ```bash
   $ docker run hello-world
   ```



8. 将当前用户(此时可以使用dev登录)加入到`docker`用户组

   - 新增`docker`用户组

     ```bash
      sudo groupadd docker
     ```

     

   - 将用户加入`docker`用户组

     ```bash
      sudo usermod -aG docker ${USER}
     ```

     

   - 重启`docker`服务

     ```bash
     sudo systemctl restart docker
     ```

   


   - 重新登录用户

​     