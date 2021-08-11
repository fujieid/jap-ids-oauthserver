package com.fujieid.oauth.handler;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局错误处理
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
public class AuthErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) throws Throwable {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            throw (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
    }

}
