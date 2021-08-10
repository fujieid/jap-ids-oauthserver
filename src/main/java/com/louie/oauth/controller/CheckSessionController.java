package com.louie.oauth.controller;

import com.fujieid.jap.ids.JapIds;
import com.fujieid.jap.ids.model.IdsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/oauth")
public class CheckSessionController {

    /**
     * 是否已经认证了
     * @param request
     * @return
     */
    @GetMapping("/check_session")
    public IdsResponse<String, Object> token(HttpServletRequest request) {
        return new IdsResponse<String, Object>().data(JapIds.isAuthenticated(request));
    }
}
