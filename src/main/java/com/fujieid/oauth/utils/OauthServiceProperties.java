package com.fujieid.oauth.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lapati5
 * @date 2021/8/4
 */
@Data
@Component
@ConfigurationProperties(prefix = "japids.config")
public class OauthServiceProperties {
    @Value("http://localhost:${server.port}")
    private String issuer;
    @Value("http://localhost:${server.port}")
    private String loginHost;
    @Value("http://localhost:${server.port}")
    private String confirmHost;
    private String jwksKeyId = "jap-jwk-keyid";
    private String loginPageUrl;
    private String confirmPageUrl;
    private Map<String, String> extraScope;
    private boolean enableCustomizeLoginPage;
    private boolean enableCustomizeConfirmPage;
    private boolean enableRedisCache;
    private boolean enableDynamicIssuer;
    private boolean enableExternalLogin;
    private boolean enableExternalConfirm;
}
