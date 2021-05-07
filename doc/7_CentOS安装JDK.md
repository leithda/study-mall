



1. 下载JDK压缩包，地址：[Java SE Development Kit 8 - Downloads (oracle.com)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)

   jdk-8u291-linux-x64.tar.gz

2. 上传至服务器

   使用XFtp或者使用VirtualBox共享文件夹等上传至虚拟机

3. 解压

   ```bash
   # 解压压缩包
   tar -zxvf jdk-8u291-linux-x64.tar.gz
   
   # 创建系统目录
   sudo mkdir /opt/soft
   
   # 移动jdk目录
   sudo mv jdk1.8.0_291/ /opt/soft/
   ```

   

4. 配置环境变量

   ```bash
   sudo vim /etc/profile
   ```

   - 添加如下内容

     ```bash
     ## Java 配置 ##
     export JAVA_HOME=/opt/soft/jdk1.8.0_291
     export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$CLASSPATH
     export PATH=$JAVA_HOME/bin:$PATH
     ```

   - 使配置文件生效`source /etc/profile`

   

5. 验证

   `java -version`