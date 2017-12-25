# Cloud Diary

### 简介
简洁的日记网站，内容在前端加密，保证了私密性。

### 基本功能
+ 用户注册、登录、找回密码以及邮件确认
+ 查看、新增、修改日记以及日记加密
+ 修改昵称、签名、性别、密码
+ 上传头像、背景图片

### 技术栈
+ 前台：Angular 4
+ 后台：Java 7、Spring Boot、Spring Data、Spring Mail、JJWT、Lombok
+ 数据库：MySQL
+ CI: git、maven、jenkins、nginx、tomcat

### HOW-TO RUN
+ 导入cloud-diary文件夹下面的*.sql初始化数据库
+ 在里J2EE容器配置data source jndi和mail jndi，如果容器没有javax.mail.jar，如tomcat,请下载。
需要配置的三个JNDI： jdbc/sso jdbc/diary mail/session
同时还需要修改sso项目配置文件的adminemail值
+ 建立/var/minidiary/文件夹,并创建子文件夹log/和image/，并开放权限是使应用能够访问，作为日志和图片存储路径
+ 在cloud-diary件文件夹下执行
 ```sh
        mvn clean install
```
+ 将SSO和Diary子项目的war包部署到J2EE容器，默认的url请配置为/sso和/diary。
如需修改，请修改ui项目的配置文件
+ 在Cloud-diary-web-ui/ui 文件夹下执行 （以下命令需要nodejs和angular-cli，如果没有，请先安装）
```sh
        npm install
        ng build --prod
``` 


+ 将在Cloud-diary-web-ui/ui/dist文件夹下的所有内容部署到具有http服务器功能的server即可
### License
---
MIT

