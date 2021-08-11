package com.fujieid.oauth.controller;

import com.fujieid.jap.ids.endpoint.DiscoveryEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/.well-known")
public class DiscoveryController {

    /**
     * 服务发现
     * @return 服务信息
     */
    @GetMapping("/openid-configuration")
    public Object configuration(HttpServletRequest request) {
        return new DiscoveryEndpoint().getOidcDiscoveryInfo(request);
    }

    /**
     * 解密公钥
     * @return 获取公钥
     */
    @GetMapping("/jwks.json")
    public String jwks() {
        return new DiscoveryEndpoint().getJwksPublicKey(null);
    }
}
