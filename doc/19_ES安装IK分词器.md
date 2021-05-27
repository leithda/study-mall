



1. 下载IK分词器

   下载路径:https://github.com/medcl/elasticsearch-analysis-ik，注意要选择与ES版本对应的分词器

2. 上传至服务器

3. 解压

   ```bash
   unzip elasticsearch-analysis-ik-7.6.2.zip -d ik
   ```

   

4. 移动至docker的ES挂载的插件目录下

   ```bash
   mv ik/ ~/data/es/plugins/
   ```

   

5. 进入容器内部查看插件是否安装成功

   ```bash
   [dev@10 plugins]$ docker exec -it elasticsearch /bin/bash
   [root@e96f05cee391 elasticsearch]pwd    
   /usr/share/elasticsearch
   [root@e96f05cee391 elasticsearch]# cd bin/
   [root@e96f05cee391 bin]# ls
   elasticsearch           elasticsearch-cli       elasticsearch-enve      elasticsearch-node           elasticsearch-setup-passwords  elasticsearch-sql-cli-7.6.2.jar  x-pack-env
   elasticsearch-certgen   elasticsearch-croneval  elasticsearch-keystore  elasticsearch-plugin         elasticsearch-shard            elasticsearch-syskeygen          x-pack-security-env
   elasticsearch-certutil  elasticsearch-env       elasticsearch-migrate   elasticsearch-saml-metadata  elasticsearch-sql-cli          elasticsearch-users              x-pack-watcher-env
   [root@e96f05cee391 bin]# ./elasticsearch-plugin list
   ik
   
   ```

   - 如图：ik分词器已经安装成功

6. 重启ES，进入服务器，执行`docker restart elasticsearch`重启ES

7. 刷新Kibana，测试分词

   ```http
   POST _analyze
   {
     "analyzer": "ik_smart",
     "text": "乔碧萝殿下"
   }
   
   ```

   ```json
   {
     "tokens" : [
       {
         "token" : "乔",
         "start_offset" : 0,
         "end_offset" : 1,
         "type" : "CN_CHAR",
         "position" : 0
       },
       {
         "token" : "碧",
         "start_offset" : 1,
         "end_offset" : 2,
         "type" : "CN_CHAR",
         "position" : 1
       },
       {
         "token" : "萝",
         "start_offset" : 2,
         "end_offset" : 3,
         "type" : "CN_CHAR",
         "position" : 2
       },
       {
         "token" : "殿下",
         "start_offset" : 3,
         "end_offset" : 5,
         "type" : "CN_WORD",
         "position" : 3
       }
     ]
   }
   
   ```

   - 可以看出，乔碧萝并没有被识别为词语，可以通过加载远程词库解决。

8. 按照 [20_Docker安装Nginx](20_Docker安装Nginx.md) 的步骤安装好Nginx，并在Nginx的html下新建es文件夹

   ```bash
   cd ~/data/nginx/html/es
   touch fenci.txt
   #######
   # 输入i进入编辑模式
   尚硅谷
   乔碧萝
   # :wq保存
   ```

9. 修改ik分词器配置加载远程词库

   ```bash
   cd ~/data/es/plugins/ik/config
   vi IKAnalyzer.cfg.xml
   ```

   修改内容如下：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
   <properties>
           <comment>IK Analyzer 扩展配置</comment>
           <!--用户可以在这里配置自己的扩展字典 -->
           <entry key="ext_dict"></entry>
            <!--用户可以在这里配置自己的扩展停止词字典-->
           <entry key="ext_stopwords"></entry>
           <!--用户可以在这里配置远程扩展字典 -->
           <entry key="remote_ext_dict">http://192.168.56.10/es/fenci.txt</entry>
           <!--用户可以在这里配置远程扩展停止词字典-->
           <!-- <entry key="remote_ext_stopwords">words_location</entry> -->
   </properties>
   
   ```

10. 重启ES，`docker restart elasticsearch`

11. 测试

    ```http
    POST _analyze
    {
      "analyzer": "ik_smart",
      "text": "乔碧萝殿下"
    }
    ```

    ```json
    {
      "tokens" : [
        {
          "token" : "乔碧萝",
          "start_offset" : 0,
          "end_offset" : 3,
          "type" : "CN_WORD",
          "position" : 0
        },
        {
          "token" : "殿下",
          "start_offset" : 3,
          "end_offset" : 5,
          "type" : "CN_WORD",
          "position" : 1
        }
      ]
    }
    
    ```

    - 成功识别出乔碧萝词语，远程词典加载成功。