FROM java:8
# 指定维护者名字
MAINTAINER louis <louisliu2048@gmail.com>
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为app.jar
ADD oauth-service-0.0.1-SNAPSHOT.jar app.jar
# 声明服务运行在8888端口
EXPOSE 8888
# 运行jar包
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]