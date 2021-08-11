# OAuth2.0授权服务使用文档

## 快速了解

### 项目简介

`oauth-service`是基于 jap-ids 开发一款上手即用的 OAuth 2.0 授权服务；支持授权码模式；授权码-PKCE模式；隐式授权模式；密码授权模式；客户端授权模式；OAuth应用创建等多种功能；

> `jap-ids` 是基于 [RFC6749 (opens new window)](https://tools.ietf.org/html/rfc6749)、[RFC7636 (opens new window)](https://tools.ietf.org/html/rfc7636)、[RFC7033 (opens new window)](https://tools.ietf.org/html/rfc7033)等标准协议和 [OpenID Connect Core 1.0 (opens new window)](https://openid.net/specs/openid-connect-core-1_0.html)认证协议，实现的一款轻量级、业务解耦、开箱即用的新一代国产授权认证框架。

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

<img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812001833974.png" alt="image-20210812001833974" style="zoom:50%;" />

4、对系统进行接口测试建议使用`Postman`等工具进行, 需要注意保持session一致

## 项目介绍

### 文件结构

> `sql`目录下是数据库文件, `config`目录下存放JapIds以及Redis配置, `controller`提供不同授权模式接口以及服务发现等功能, `service`和`mapper`负责于持久层和Redis的交互访问, `templates`中为指自定义的登录和确认页面(需在配置文件中开启自定义后生效) , `application.yml`为总配置文件
>
> <img src="https://cdn.jsdelivr.net/gh/Lapati5/PictureBed@main/img/image-20210812002430441.png" alt="image-20210812002430441" style="zoom:50%;" />

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
# LOG
logging:
  level:
    root: info
```

其中可以通过`ids.oauth2.xxx`配置oauth授权服务(不配置则使用默认值)

```yaml
# oauth2.0服务配置
ids:
  oauth2:
    # 登录/确认外部的页面的host(默认为http://localhost:${server.port})
    host:
    # 发行者地址(默认为http://localhost:${server.port})
    issuer:
    # 是否开启redis缓存(默认使用本地缓存)
    enable-redis-cache: 
    # 开启使用自定义登录页面(默认不开启)
    enable-customize-login-page: 
    # 开启使用自定义确认页面(默认不开启)
    enable-customize-confirm-page: 
    # 自定义登录页面路径
    login-page-url:
    # 自定义确认页面路径
    confirm-page-url:
    # 配置JwksKeyId(默认为"jap-jwk-keyid")
    jwks-key-id:
```

### 核心代码

> JapIdsConfiguration配置类注册Ids上下文

```java
@Configuration
public class JapIdsConfiguration implements ApplicationListener<ApplicationStartedEvent> {
	//...
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
    }
    //...
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
    public static String HOST;
    public static String LOGIN_PAGE;
    public static String CONFIRM_PAGE;
    public static String JWKS_KEY_ID;
    public static JapCache JAP_CACHE;

    @Override
    public void afterPropertiesSet() throws Exception {
        ISSUER = oauthServiceProperties.getIssuer();
        HOST = oauthServiceProperties.getHost();

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

> OAuth 服务端为客户端提供以下几个接口(对应controller)：

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





