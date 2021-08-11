package com.fujieid.oauth.utils;

import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.oauth.service.IdsCacheImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lapati5
 * @date 2021/8/10
 */
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
