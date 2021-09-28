package com.fujieid.oauth.config;

import com.fujieid.jap.ids.JapIds;
import com.fujieid.jap.ids.config.IdsConfig;
import com.fujieid.jap.ids.config.JwtConfig;
import com.fujieid.jap.ids.context.IdsContext;
import com.fujieid.jap.ids.filter.IdsAccessTokenFilter;
import com.fujieid.jap.ids.filter.IdsUserStatusFilter;
import com.fujieid.jap.ids.model.IdsScope;
import com.fujieid.jap.ids.model.enums.TokenSigningAlg;
import com.fujieid.jap.ids.provider.IdsScopeProvider;
import com.fujieid.jap.ids.service.IdsClientDetailService;
import com.fujieid.jap.ids.service.IdsIdentityService;
import com.fujieid.jap.ids.service.IdsUserService;
import com.fujieid.jap.ids.util.JwkUtil;
import com.fujieid.oauth.utils.JapIdsConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author lapati5
 * @date 2021/8/4
 */
@Configuration
public class JapIdsConfiguration implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private IdsClientDetailService idsClientDetailService;
    @Autowired
    private IdsIdentityService idsIdentityService;
    @Autowired
    private IdsUserService idsUserService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        // 注册 JAP IDS 上下文
        String loginHost = JapIdsConstUtil.LOGIN_HOST;
        String confirmHost = JapIdsConstUtil.CONFIRM_HOST;
        JapIds.registerContext(new IdsContext()
                .setUserService(idsUserService)
                .setClientDetailService(idsClientDetailService)
                .setIdentityService(idsIdentityService)
                //如果启动redis将会将容器中的IdsCache set进去
                .setCache(JapIdsConstUtil.JAP_CACHE)
                .setIdsConfig(new IdsConfig()
                        /*
                            所需要的配置均可以在application配置文件中进行配置
                            （ids.oauth2.*）,运行时动态获取属性
                         */
                        .setIssuer(JapIdsConstUtil.ISSUER)
                        .setEnableDynamicIssuer(JapIdsConstUtil.IS_DYNAMIC)
                        .setConfirmPageUrl(confirmHost + JapIdsConstUtil.CONFIRM_PAGE)
                        .setExternalConfirmPageUrl(JapIdsConstUtil.IS_CONFIRM_EXTERNAL)
                        .setLoginPageUrl(loginHost + JapIdsConstUtil.LOGIN_PAGE)
                        .setExternalLoginPageUrl(JapIdsConstUtil.IS_LOGIN_EXTERNAL)

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
            IdsScopeProvider.addScope(new IdsScope().
                    setCode(scope.getKey()).setDescription(scope.getValue()));
        }
    }

    /**
     * token过滤器
     * @return 过滤器
     */
    @Bean
    public FilterRegistrationBean<IdsAccessTokenFilter> registerAccessTokenFilter() {
        FilterRegistrationBean<IdsAccessTokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new IdsAccessTokenFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("ignoreUrl",
                "/," +
                        "/oauth/login," +
                        "/oauth/login/customize," +
                        "/oauth/logout," +
                        "/oauth/error," +
                        "/oauth/confirm," +
                        "/oauth/confirm/customize," +
                        "/oauth/authorize," +
                        "/oauth/authorize/auto," +
                        "/oauth/token," +
                        "/oauth/check_token," +
                        "/oauth/check_session," +
                        "/oauth/registration," +
                        "/oauth/pkce/**," +
                        "/oauth/client/**," +
                        "/.well-known/jwks.json," +
                        "/.well-known/openid-configuration"
        );
        registration.setName("IdsAccessTokenFilter");
        registration.setOrder(1);
        return registration;
    }

    /**
     * 用户身份过滤器
     * @return 过滤器
     */
    @Bean
    public FilterRegistrationBean<IdsUserStatusFilter> registerUserStatusFilter() {
        FilterRegistrationBean<IdsUserStatusFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new IdsUserStatusFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("ignoreUrl",
                "/," +
                        "/oauth/login," +
                        "/oauth/login/customize," +
                        "/oauth/logout," +
                        "/oauth/error," +
                        "/oauth/confirm," +
                        "/oauth/confirm/customize," +
                        "/oauth/authorize," +
                        "/oauth/authorize/auto," +
                        "/oauth/token," +
                        "/oauth/check_session," +
                        "/oauth/registration," +
                        "/oauth/pkce/**," +
                        "/oauth/client/**," +
                        "/oauth/userinfo," +
                        "/.well-known/jwks.json," +
                        "/.well-known/openid-configuration"
        );
        registration.setName("IdsUserStatusFilter");
        registration.setOrder(1);
        return registration;
    }
}
