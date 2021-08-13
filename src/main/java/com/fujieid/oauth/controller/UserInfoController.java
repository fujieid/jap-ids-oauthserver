package com.fujieid.oauth.controller;

import com.fujieid.jap.ids.endpoint.UserInfoEndpoint;
import com.fujieid.jap.ids.model.IdsResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/oauth")
public class UserInfoController {

    @RequestMapping("/userinfo")
    public IdsResponse<String, Object> userinfo(HttpServletRequest request) {
        return new UserInfoEndpoint().getCurrentUserInfo(request);
    }

}
