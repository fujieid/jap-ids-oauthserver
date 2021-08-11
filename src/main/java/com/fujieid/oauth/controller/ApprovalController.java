package com.fujieid.oauth.controller;

import com.fujieid.jap.ids.JapIds;
import com.fujieid.jap.ids.endpoint.ApprovalEndpoint;
import com.fujieid.jap.ids.model.ClientDetail;
import com.fujieid.jap.ids.model.IdsResponse;
import com.fujieid.jap.ids.util.ObjectUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/oauth")
public class ApprovalController {

    /**
     * 确认授权, 登录完成后的确认授权页面
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/confirm")
    public void confirm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ApprovalEndpoint approvalEndpoint = new ApprovalEndpoint();
        approvalEndpoint.showConfirmPage(request, response);
    }


    /**
     * 自定义授权页面，需要通过 <code>JapIds.registerContext(new IdsContext().setIdsConfig(new IdsConfig().setConfirmPageUrl(host + "/oauth/confirm/customize")</code> 配置授权页面的入口
     *
     * @param request
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/confirm/customize")
    public ModelAndView confirmCustomize(HttpServletRequest request, Model model) throws IOException {
        ApprovalEndpoint approvalEndpoint = new ApprovalEndpoint();

        IdsResponse<String, Map<String, Object>> getAuthClientInfo = approvalEndpoint.getAuthClientInfo(request);
        Map<String, Object> data = getAuthClientInfo.getData();
        ClientDetail clientDetail = (ClientDetail) data.get("appInfo");
        List<Map<String, Object>> scopeInfos = (List<Map<String, Object>>) data.get("scopes");
        String requestPath = ObjectUtils.appendIfNotEndWith(JapIds.getIdsConfig().getAuthorizeUrl(), "?") + request.getQueryString();
        model.addAttribute("clientDetail", clientDetail);
        model.addAttribute("scopeInfos", scopeInfos);
        model.addAttribute("requestPath", requestPath);
        return new ModelAndView("confirm");
    }


}
