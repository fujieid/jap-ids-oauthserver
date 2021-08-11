package com.fujieid.oauth.controller;

import com.fujieid.jap.ids.endpoint.ErrorEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/oauth")
public class ErrorController {

    /**
     * 授权异常处理
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new ErrorEndpoint().showErrorPage(request, response);
    }

}
