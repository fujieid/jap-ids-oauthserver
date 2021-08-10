package com.louie.oauth.utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lapati5
 * @date 2021/8/4
 */
@Data
@Component
@ConfigurationProperties(prefix = "ids.oauth2")
public class OauthServiceProperties {
    @Value("http://localhost:${server.port}")
    private String issuer;
    @Value("http://localhost:${server.port}")
    private String host;
    private String jwksKeyId = "jap-jwk-keyid";
    private String loginPageUrl;
    private String confirmPageUrl;
    private boolean enableCustomizeLoginPage;
    private boolean enableCustomizeConfirmPage;
}
