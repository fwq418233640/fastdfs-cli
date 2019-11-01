# fastdfs-cli
fastdfs-client spring boot 版本 提供ui界面以及 Resultfull Api 接口
```
# setting maven 3.5.x
mvn clean install 

cd target
java -jar -D${TRACKER_ADDR} fastdfs-cli-1.0.jar
```

# 参数配置
``` 
# 服务器端口
SERVER_PORT

# tracker IP 地址
TRACKER_ADDR 

# 文件临时存储路径 默认为 System.getProperty("java.io.tmpdir")
FILE_PATH
 
# ui 界面登陆账号密码
# 默认值 admin
USER_NAME
PASS_WORD
```

# 设置数据库
````
默认采用 HsqlDB 作为内嵌数据库
但当数据量过大时 就需更改
application-db.yml 中提供了 数据库相关配置
````
