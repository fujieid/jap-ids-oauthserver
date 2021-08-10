package com.louie.oauth.controller;

import com.fujieid.jap.ids.JapIds;
import com.fujieid.jap.ids.endpoint.LoginEndpoint;
import com.fujieid.jap.ids.model.IdsResponse;
import com.fujieid.jap.ids.util.ObjectUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lapati5
 * @date 2021/8/6
 */
@RestController
@RequestMapping("/oauth")
public class LoginController {

    /**
     * 跳转到登录页面
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/login")
    public void toLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new LoginEndpoint().showLoginPage(request, response);
    }

    /**
     * 执行登录操作
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public RedirectView login(HttpServletRequest request, HttpServletResponse response) {
        IdsResponse<String, String> idsResponse = new LoginEndpoint().signin(request, response);
        return new RedirectView(idsResponse.getData());
    }


    /**
     * 自定义授权页面，需要通过 <code>JapIds.registerContext(new IdsContext().setIdsConfig(new IdsConfig().setLoginPageUrl(host + "/oauth/login/customize")</code> 配置登录页面的入口
     *
     * @param request
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/login/customize")
    public ModelAndView loginCustomize(HttpServletRequest request, Model model) throws IOException {
        String authenticationUrl = ObjectUtils.appendIfNotEndWith(JapIds.getIdsConfig().getLoginUrl(), "?") + request.getQueryString();
        model.addAttribute("requestPath", authenticationUrl);
        model.addAttribute("usernameField", JapIds.getIdsConfig().getUsernameField());
        model.addAttribute("passwordField", JapIds.getIdsConfig().getPasswordField());
        return new ModelAndView("login");
    }
}
