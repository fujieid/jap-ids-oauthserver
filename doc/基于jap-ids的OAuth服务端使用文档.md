# OAuth2.0授权服务使用文档

## 快速了解

### 项目简介

> `jap-ids` 是基于 [RFC6749 (opens new window)](https://tools.ietf.org/html/rfc6749)、[RFC7636 (opens new window)](https://tools.ietf.org/html/rfc7636)、[RFC7033 (opens new window)](https://tools.ietf.org/html/rfc7033)等标准协议和 [OpenID Connect Core 1.0 (opens new window)](https://openid.net/specs/openid-connect-core-1_0.html)认证协议，实现的一款轻量级、业务解耦、开箱即用的新一代国产授权认证框架。

`oauth-service`是基于 jap-ids 开发一款上手即用的 OAuth 2.0 授权服务；支持授权码模式；授权码-PKCE模式；隐式授权模式；密码授权模式；客户端授权模式；OAuth应用创建等多种功能；

### 主要特性

- 支持自定义登陆界面和授权确认界面，仅通过简单配置即可完成
- 支持多种授权模式，可以根据请求类型进行授权操作
- 支持Redis/Memory方式对授权过程中code，token等数据进行缓存，提升性能
- 为用户提供OAuth应用创建服务与运行时动态更新，存储于后台数据库中
- 提供对客户端的接口以及对资源服务器的访问认证

### 涉及技术

1、系统环境

- JDK 8
- Servlet 3.0
- Apache Maven 3
- MySQL 8.0.x
- Redis 6.2.x

2、主框架

- jap-ids 1.0.3
- Spring Boot 2.4.x

3、持久层

- Apache MyBatis 3.5.x
- Alibaba Druid 1.2.x

4、视图层

- Thymeleaf 3.0.x

## 环境部署

### 准备工作

```text
JDK >= 1.8 (推荐1.8版本)
Mysql >= 5.7.0 (推荐8.0版本)
Maven >= 3.0
```

### 必要配置

- 修改数据库连接，编辑`resources`目录下的`application.yml`

  ```yaml
  # mysql数据库配置
  spring:
    datasource:
      type: com.alibaba.druid.pool.DruidDataSource
      driver-class-name: com.mysql.cj.jdbc.Driver
      druid:
        url: jdbc:mysql://${数据库地址}/jap_ids_db?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
        username: ${数据库用户名}
        password: ${数据库密码}
  ```

- 修改服务器配置，编辑`resources`目录下的`application.yml`

  ```yaml
  server:
    port: ${端口号} # 默认是8888
  ```

- 修改redis配置，编辑`resources`目录下的`application.yml`（如果启用）

  ```yaml
  spring:
    redis:
      database: 0
      host: ${redis主机名(默认为localhost)}
      port: ${redis端口号(默认为6379)}
  ```

### 运行系统

1、创建数据库`jap_ids_db`并导入数据脚本`client_detail.sql`，`user_info.sql`

2、运行`com.louie.oauth.OauthServiceApplication`项目, 当出现下方提示表示启动成功

```
========启动成功=========
http://localhost:8888
```

3、打开浏览器进行测试，输入url地址http://localhost:8888看到下图, 点击测试环境, 跳转到登录界面输入用户名`user`和密码`12345`登录进行授权确认, 返回该界面获得code则说明配置成功

<img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210813130633684.png" alt="image-20210813130633684" style="zoom:50%;" />

4、对系统进行接口测试建议使用`Postman`等工具进行, 需要注意保持session一致

## 项目介绍

### 文件结构

> `sql`目录下是数据库文件, `config`目录下存放JapIds以及Redis配置, `controller`提供不同授权模式接口以及服务发现等功能, `service`和`mapper`负责于持久层和Redis的交互访问, `templates`中为指自定义的登录和确认页面(需在配置文件中开启自定义后生效) , `application.yml`为总配置文件, `docker`目录存放Dockerfile文件用于项目容器化
>
> <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210929112904124.png" alt="image-20210929112904124" style="zoom:50%;" />

### 配置文件

```yaml
server:
  port: 8888

spring:
  application:
    name: oauth-service
  # mysql数据库配置
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    druid:
      url: jdbc:mysql://localhost:3306/jap_ids_db?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
      username: root
      password: 12345
  # redis配置
  redis:
    database: 0
    host: localhost
    port: 6379
    timeout: 6000ms  # 连接超时时长（毫秒）
  # thymeleaf
  thymeleaf:
    cache: false

# mybatis配置
mybatis:
  mapper-locations: classpath:mapper/**/*.xml
# LOG日志
logging:
  level:
    root: info
```

其中可以通过`ids.oauth2.xxx`配置oauth授权服务(不配置则使用默认值)

```yaml
# oauth2.0服务配置
ids:
  oauth2:    
    host: # 登录/确认外部的页面的host(默认为http://localhost:${server.port})   
    
    issuer: #  发行者地址(默认为http://localhost:${server.port})    
    
    enable-redis-cache: # 是否开启redis缓存(默认使用本地缓存)    
    
    enable-customize-login-page: # 开启使用服务器内部自定义登录页面(默认不开启)    
    
    enable-customize-confirm-page: # 开启使用服务器内部自定义确认页面(默认不开启)  
    
    enable-external-confirm: # 开启外部确认页面
        
    enable-external-login:  #开启外部登录页面
    
    enable-dynamic-issuer: # 开启动态issur使用服务端的端口链接
    
    login-page-url: # 自定义登录页面路径
    
    confirm-page-url: # 自定义确认页面路径
    
    jwks-key-id: # 配置JwksKeyId(默认为"jap-jwk-keyid")
    
    extra-scope: # 新增scope, 以map形式保存
```

> 其中自定义页面通过命名为`login`, `index`和`confirm`等html文件放入`templates`文件夹中可以自行设定; 同时error页面也可以在`templates/error`中自定义设置 (**页面自定义需要在上述配置中开启后生效**)

### 核心代码

> JapIdsConfiguration配置类注册Ids上下文

```java
@Configuration
public class JapIdsConfiguration implements ApplicationListener<ApplicationStartedEvent> {
	...
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        // 注册 JAP IDS 上下文
        String host = JapIdsConstUtil.HOST;
        JapIds.registerContext(new IdsContext()
                .setUserService(idsUserService)
                .setClientDetailService(idsClientDetailService)
                .setIdentityService(idsIdentityService)
                //如果配置启动redis将会将容器中的放入IdsCacheImpl
                .setCache(JapIdsConstUtil.JAP_CACHE)
                .setIdsConfig(new IdsConfig()
                        /*
                            所需要的配置均可以在application配置文件中进行配置
                            （ids.oauth2.*）,运行时动态获取属性
                         */
                        .setIssuer(JapIdsConstUtil.ISSUER)
                        .setConfirmPageUrl(host + JapIdsConstUtil.CONFIRM_PAGE)
                        .setExternalConfirmPageUrl(true)
                        .setLoginPageUrl(host + JapIdsConstUtil.LOGIN_PAGE)
                        .setExternalLoginPageUrl(true)
						//jwk配置, 默认使用RS256加密
                        .setJwtConfig(new JwtConfig()
                                .setJwksKeyId(JapIdsConstUtil.JWKS_KEY_ID)
                                .setJwksJson(JwkUtil.createRsaJsonWebKeySetJson(JapIdsConstUtil.JWKS_KEY_ID, TokenSigningAlg.RS256))
                        )
                )
        );
        // 配置 ids 支持的 scope, 系统默认支持以下 scope： read、write、openid、email、phone
        // 如果需要追加 scope，可以使用 addScope
        Map<String, String> extraScopes = JapIdsConstUtil.EXTRA_SCOPE;
        for (Map.Entry<String, String> scope : extraScopes.entrySet()) {
            IdsScopeProvider.addScope(new IdsScope()
                    .setCode(scope.getKey()).setDescription(scope.getValue()));
        }
    }
    ...
}
```

配置中的值均通过`JapIdsConstUtil`工具类获取, 该工具类通过注入配置文件绑定的属性类`OauthServiceProperties`获取其中的属性

> JapIdsConstUtil通过OauthServiceProperties获取定义属性, 开发者可以在此基础上进行扩展

```java
@Component
public class JapIdsConstUtil implements InitializingBean {
     @Autowired
    private OauthServiceProperties oauthServiceProperties;
    @Autowired
    private IdsCacheImpl idsCache;

    public static String ISSUER;
    public static String LOGIN_HOST;
    public static String CONFIRM_HOST;
    public static String LOGIN_PAGE;
    public static String CONFIRM_PAGE;
    public static String JWKS_KEY_ID;
    public static Map<String, String> EXTRA_SCOPE;
    public static JapCache JAP_CACHE;
    public static boolean IS_DYNAMIC;
    public static boolean IS_LOGIN_EXTERNAL;
    public static boolean IS_CONFIRM_EXTERNAL;

    @Override
    public void afterPropertiesSet() throws Exception {
        //bean完成容器注入后进行
        ISSUER = oauthServiceProperties.getIssuer();
        LOGIN_HOST = oauthServiceProperties.getLoginHost();
        CONFIRM_HOST = oauthServiceProperties.getConfirmHost();

        LOGIN_PAGE = oauthServiceProperties.getLoginPageUrl();
        if (LOGIN_PAGE == null || LOGIN_PAGE.length() == 0) {
            LOGIN_PAGE = oauthServiceProperties.isEnableCustomizeLoginPage() ? "/oauth/login/customize" : "/oauth/login";
        }
        CONFIRM_PAGE = oauthServiceProperties.getConfirmPageUrl();
        if (CONFIRM_PAGE == null || CONFIRM_PAGE.length() == 0) {
            CONFIRM_PAGE = oauthServiceProperties.isEnableCustomizeConfirmPage() ? "/oauth/confirm/customize" : "/oauth/confirm";
        }

        JWKS_KEY_ID = oauthServiceProperties.getJwksKeyId();
        JAP_CACHE = oauthServiceProperties.isEnableRedisCache() ? idsCache : null;
        IS_DYNAMIC = oauthServiceProperties.isEnableDynamicIssuer();
        IS_LOGIN_EXTERNAL = oauthServiceProperties.isEnableExternalLogin();
        if (!IS_LOGIN_EXTERNAL) {
            LOGIN_HOST = "";
        }
        IS_CONFIRM_EXTERNAL = oauthServiceProperties.isEnableExternalConfirm();
        if (!IS_CONFIRM_EXTERNAL) {
            CONFIRM_HOST = "";
        }

        EXTRA_SCOPE = oauthServiceProperties.getExtraScope();
    }
}
```

> OAuth服务端提供对客户端信息进行操作接口

```java
@Service
public class IdsClientDetailServiceImpl implements IdsClientDetailService {

    @Autowired
    private IdsClientDetailMapper idsClientDetailMapper;

    @Override
    public ClientDetail getByClientId(String clientId) {
        return idsClientDetailMapper.getByClientId(clientId);
    }

    
    @Override
    public ClientDetail add(ClientDetail clientDetail) {
        //生成clientId和ClientSecret
        clientDetail.setId(UUID.randomUUID().toString().replace("-", ""))
                    .setClientId(OauthUtil.generateClientId())
                    .setClientSecret(OauthUtil.generateClientSecret())
                    .setAvailable(true);
        //默认允许获取所有信息
        if (StringUtil.isEmpty(clientDetail.getScopes())) {
            clientDetail.setScopes(String.join(" ", IdsScopeProvider.getScopeCodes()));
        }
        //默认适用所有授权模式
        if (StringUtil.isEmpty(clientDetail.getGrantTypes()) || StringUtil.isEmpty(clientDetail.getResponseTypes())) {
            clientDetail.setGrantTypes(String.join(" ", GrantType.grantTypes()));
            clientDetail.setResponseTypes(String.join(" ", Arrays.asList(ResponseType.CODE.getType(), ResponseType.TOKEN.getType(), ResponseType.ID_TOKEN.getType(), ResponseType.NONE.getType())));
        }
        //存入数据库中
        idsClientDetailMapper.add(clientDetail);
        return clientDetail;
    }

    @Override
    public ClientDetail update(ClientDetail clientDetail) {...}

    @Override
    public boolean removeById(String id) {...}

    @Override
    public boolean removeByClientId(String clientId) {...}

    @Override
    public List<ClientDetail> getAllClientDetail() {...}
}
```

当客户端没有明确授权方式时默认使用授权码模式, 没有明确scope获取默认所有信息

> 自定义的IdsCacheImpl使用Redis进行缓存, Redis的配置可见`/config/RedisConfig`

```java
@Component
public class IdsCacheImpl implements JapCache {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void set(String s, Serializable serializable) {
        redisTemplate.opsForValue().set(s, serializable);
    }

    @Override
    public void set(String s, Serializable serializable, long l) {
        redisTemplate.opsForValue().set(s, serializable, l);
    }

    @Override
    public Serializable get(String s) {
        return redisTemplate.opsForValue().get(s);
    }

    @Override
    public boolean containsKey(String s) {
        return redisTemplate.hasKey(s);
    }

    @Override
    public void removeKey(String s) {
        redisTemplate.delete(s);
    }
}
```

### 相关接口

> OAuth Server为客户端提供以下几个接口完成授权服务(对应Controller)：

**DiscoveryController**

- `GET`服务发现：http://{host}:{port}/.well-known/openid-configuration
- `GET`解密公钥：http://{host}:{port}/.well-known/jwks.json

**AuthorizationController**

- `GET`获取授权：http://{host}:{port}/oauth/authorize 

**PkceController**

- `GET/POST`使用**PKCE**获取code：http://{host}:{port}/oauth/pkce/authorize
- `GET/POST`使用**PKCE**获取token：http://{host}:{port}/oauth/pkce/token

**TokenController**

- `GET/POST`获取/刷新Token：http://{host}:{port}/oauth/token
- `GET/POST`收回Token：http://{host}:{port}/oauth/revoke_token

**UserInfoController**

- `GET/POST`用户详情：http://{host}:{port}/oauth/userinfo

**CheckSessionController**

- `GET`check session：http://{host}:{port}/oauth/check_session

**LogoutController**

- `GET`退出登录：http://{host}:{port}/oauth/logout

**ClientController**

- `POST`注册client信息：http://{host}:{port}/oauth/client/register
- `PUT`更新client信息：http://{host}:{port}/oauth/client/update
- `Get`根据clientId获取client信息：http://{host}:{port}/oauth/client/{clientId}
- `DELETE`根据id删除client信息：http://{host}:{port}/oauth/client/removeById/{id}
- `DELETE`根据clientId删除client信息：http://{host}:{port}/oauth/client//removeByClientId/{clientId}

**AuthErrorController / AuthExceptionHandler**

- 处理Web错误页面以及统一异常处理

### 授权码模式演示

> 使用客户端访问进行授权认证接口及参数(`以授权码模式为例`)

- `GET`获取授权：http://{host}:{port}/oauth/authorize(自动授权需要在URL上添加`autoapprove=true`参数, 同时保证客户端自动授权的开启)

  <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812002606597.png" alt="image-20210812002606597" style="zoom:50%;" />

- 跳转到登录界面, 输入用户名和密码(根据`user_info`中的数据默认为`user`和`12345`), 界面可以根据需求自定义完成

  > 用户密码在数据库中的存储经过了sha-256加密并前20位存储

  <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812002530762.png" alt="image-20210812002530762" style="zoom:50%;" />

- 登陆后在确认授权页面(非自动授权的情况下)完成用户信息的授权, 返回`code`, 同样页面也可以自定义

  <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812002657092.png" alt="image-20210812002657092" style="zoom: 33%;" />

- `GET/POST`获取/刷新Token：http://{host}:{port}/oauth/token

  <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812002736567.png" alt="image-20210812002736567" style="zoom:50%;" />

  <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812002754711.png" alt="image-20210812002754711" style="zoom:50%;" />

- `GET/POST`获取用户详情：http://{host}:{port}/oauth/userinfo

  <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812002843829.png" alt="image-20210812002843829" style="zoom:50%;" />

  <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812001434255.png" alt="image-20210812001434255" style="zoom:50%;" />

## 容器化部署

### 容器化简介

`容器技术（Linux Container，LXC）`是一种内核轻量级的操作系统层虚拟化技术。容器提供了将应用程序的代码、运行时、系统工具、系统库和配置打包到一个实例中的标准方法。一个宿主机上的所有容器共享一个内核（操作系统）。容器化就是把应用程序打包成容器运行，是应用程序级别的虚拟化。容器具有以下特点：

- 极其轻量。容器仅打包应用程序、配置和依赖库，所占存储少，一般为MB级别。
- 秒级启动。容器启动时间为秒级，虚拟机为分钟级，可大大减少开发、测试、部署的时间。
- 易于移植。一次构建、随处部署，完全离线的环境也是如此。
- 接近原生。在容器内运行的应用程序，性能接近原生，远高于虚拟机内运行的程序。

### OAuth Server容器化

> 我们提供用户可以借助docker完成服务端容器化部署的方法, 使得基于jap-ids的oauth server更加容易让别人体验

在该项目`src/main/docker`目录下编写Dockerfile生成server项目镜像

```dockerfile
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
```

在`pom.xml`文件中提供maven的docker插件配置, 调用`docker:build`可以将服务打包生成的jar包直接生成镜像发送至目标服务器上(需要先在远程/本地docker服务器上打开2375端口, 才可以在网络上对docker进行操作)

```xml
<!-- Docker -->
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>1.0.0</version>
    <!-- 将插件绑定在某个phase执行 -->
    <executions>
        <execution>
            <id>build-image</id>
            <!-- 用户只需执行mvn package ，就会自动执行mvn docker:build -->
            <phase>package</phase>
            <goals>
                <goal>build</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <!-- 指定生成的镜像名 -->
        <imageName>${docker.image.prefix}/${project.artifactId}:${project.version}</imageName>
        <!-- 指定标签 -->
        <imageTags>
            <imageTag>${project.version}</imageTag>
        </imageTags>
        <!-- 指定 Dockerfile 路径 -->
        <dockerDirectory>src/main/docker</dockerDirectory>
        <!-- 指定远程 docker api地址 -->
        <dockerHost>http://${ip.address}:2375</dockerHost>
        <resources>
            <resource>
                <targetPath>/</targetPath>
                <!-- jar包所在的路径此处配置的对应target目录 -->
                <directory>${project.build.directory}</directory>
                <!-- 需要包含的jar包,这里对应的是Dockerfile中添加的文件名　-->
                <include>${project.build.finalName}.jar</include>
            </resource>
        </resources>
    </configuration>
</plugin>
```

其中`${docker.image.prefix}`在下方`properties`中指定

```xml
<properties>
    <java.version>1.8</java.version>
    <docker.image.prefix>ids-server</docker.image.prefix>
</properties>
```

之后只需通过`mvn package`对项目进行打包, 通过插件配置会自动生成镜像发送到我们配置的服务器上

```
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< com.fujieid:oauth-service >----------------------
[INFO] Building oauth-service 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
...
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ oauth-service ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 24 source files to 
...
INFO] Building image ids-server/oauth-service:0.0.1-SNAPSHOT
Step 1/6 : FROM java:8

 ---> d23bdf5b1b1b
Step 2/6 : MAINTAINER louis <louisliu2048@gmail.com>

 ---> Running in 9f3426b07a70
Removing intermediate container 9f3426b07a70
 ---> e31f38ae3c23
Step 3/6 : VOLUME /tmp

 ---> Running in a0b1af0f2258
Removing intermediate container a0b1af0f2258
 ---> ba727fe360d9
Step 4/6 : ADD oauth-service-0.0.1-SNAPSHOT.jar app.jar

 ---> 1c6585e790e7
Step 5/6 : EXPOSE 8888
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  54.991 s
[INFO] Finished at: 2021-09-29T09:15:12+08:00
[INFO] ------------------------------------------------------------------------
```

![image-20210929094755386](https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210929094755386.png)

> Redis服务部署

oauth-server镜像生成后, 通过docker镜像redis部署, 执行命令

```bash
docker run -d --name oauth-redis -p 6379:6379 redis --requirepass 'password'
```

如果要开启redis的持久化则加上 –appendonly yes 完成命令如下：

```bash
docker run -d --name oauth-redis -p 6379:6379 redis --requirepass 'password' --appendonly yes
```

如果不加–appendonly yes则默认开启redis的RDB模式，如果加了则开启AOF模式。 redis服务测试：

```bash
docker exec -it 容器id redis-cli -h 127.0.0.1 -p 6379
```

进入redis后验证密码的命令为： auth password（自己设置的密码）

> 项目部署

```
docker run -it --net=host --name oauth-service -p 8888:8888  ${上面生成的容器名}
```

等待docker容器启动之后, 就可以通过`IP` + `8888`端口对远程服务器中docker容器的ids授权服务器进行访问了, 部署的redis用于存放token以及code。