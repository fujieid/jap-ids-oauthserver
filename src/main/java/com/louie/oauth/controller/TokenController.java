package com.louie.oauth.controller;

import com.fujieid.jap.ids.endpoint.TokenEndpoint;
import com.fujieid.jap.ids.model.IdsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/oauth")
public class TokenController {

    /**
     * 获取/刷新token
     * @param request request
     * @return 返回封装
     * @throws IOException IO异常
     */
    @RequestMapping("/token")
    public IdsResponse<String, Object> token(HttpServletRequest request) throws IOException {
        return new TokenEndpoint().getToken(request);
    }

    /**
     * 收回token
     * @param request request
     * @return 返回封装
     * @throws IOException IO异常
     */
    @GetMapping("/revoke_token")
    public IdsResponse<String, Object> revokeToken(HttpServletRequest request) throws IOException {
        return new TokenEndpoint().revokeToken(request);
    }

}
