package com.louie.oauth.service;

import com.fujieid.jap.ids.config.JwtConfig;
import com.fujieid.jap.ids.service.IdsIdentityService;
import org.springframework.stereotype.Service;

/**
 * @author lapati5
 * @date 2021/8/4
 */
@Service
public class IdsIdentityServiceImpl implements IdsIdentityService {
    @Override
    public String getJwksJson(String identity) {
        return IdsIdentityService.super.getJwksJson(identity);
    }

    @Override
    public JwtConfig getJwtConfig(String clientId) {
        return IdsIdentityService.super.getJwtConfig(clientId);
    }
}
