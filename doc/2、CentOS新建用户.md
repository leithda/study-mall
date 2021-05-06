



## 添加用户

1. 添加新用户 `dev`

   ```bash
   useradd dev
   ```

2. 设置密码

   ```bash
   passwd dev
   # 根据提示输入两次密码
   ```

3. 为该用户指定主目录(第1步会默认创建，无需更改)

   ```bash
   usermod -d /home/username username
   ```



## 授予`sudo`的权限

1. 查看sudo配置文件权限

   ```bash
   ls -l /etc/sudoers
   ```

2. 添加`w`权限

   ```bash
   chmod u+w /etc/sudoers
   ```

3. 编辑`sudoers`文件，添加用户

   ```bash
   vim /etc/sudoers
   ```

   找到文件中的如下内容，复制一行并将root修改为要修改的账户

   ```txt
   Allow root to run any commands anywhere
   root    ALL=(ALL)       ALL
   ```

4. 将写权限收回

   ```bash
   chmod u-w /etc/sudoers
   ```



## 更改文件所有者及组

- 更改所有者

  ```bash
  chown dev FILE
  ```

  

- 更改文件所属组信息

  ```bash
  chgrp {GROUP} FILE 
  ```



