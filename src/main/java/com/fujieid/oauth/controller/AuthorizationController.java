package com.fujieid.oauth.controller;

import com.fujieid.jap.ids.endpoint.AuthorizationEndpoint;
import com.fujieid.jap.ids.model.IdsResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lapati5
 * @date 2021/8/6
 */
@Controller
@RequestMapping("/oauth")
public class AuthorizationController {

    /**
     * 获取授权, 跳转页面（登录页面或者回调页面）
     * @param request
     * @return
     */
    @GetMapping("/authorize")
    public RedirectView authorize(HttpServletRequest request) throws IOException {
        IdsResponse<String, String> idsResponse = new AuthorizationEndpoint().authorize(request);
        return new RedirectView(idsResponse.getData());
    }

    /**
     * 同意授权（在确认授权之后）
     * @param request
     * @return
     */
    @PostMapping("/authorize")
    public RedirectView doAuthorize(HttpServletRequest request) {
        IdsResponse<String, String> idsResponse = new AuthorizationEndpoint().agree(request);
        return new RedirectView(idsResponse.getData());
    }


    /**
     * 自动授权, 跳过确认授权页面
     * 通过请求参数autoapprove=true访问
     * @param request
     * @return
     */
    @GetMapping("/authorize/auto")
    public RedirectView doAuthorizeAutoApprove(HttpServletRequest request) {
        return this.doAuthorize(request);
    }

}
