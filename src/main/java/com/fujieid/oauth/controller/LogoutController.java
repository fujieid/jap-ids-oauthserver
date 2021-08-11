package com.fujieid.oauth.controller;

import com.fujieid.jap.ids.endpoint.LogoutEndpoint;
import com.fujieid.jap.ids.model.IdsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/oauth")
public class LogoutController {

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
        IdsResponse<String, String> idsResponse = new LogoutEndpoint().logout(request, response);

        return new RedirectView(idsResponse.getData());
    }

}
