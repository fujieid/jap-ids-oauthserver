package com.fujieid.oauth.controller;

import com.fujieid.jap.ids.model.ClientDetail;
import com.fujieid.jap.ids.model.IdsResponse;
import com.fujieid.jap.ids.service.IdsClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lapati5
 * @date 2021/8/6
 */
@RestController
@RequestMapping("/oauth/client")
public class ClientController {

    @Autowired
    private IdsClientDetailService idsClientDetailService;

    /**
     * 注册client的详细信息获得clientId和clientSecret
     * @param clientDetail client信息
     * @return 返回包含clientId和clientSecret的信息
     */
    @PostMapping("/register")
    public IdsResponse<String, Object> registerClient(@RequestBody ClientDetail clientDetail) {
        ClientDetail detail = idsClientDetailService.add(clientDetail);
        return new IdsResponse<String, Object>().data(detail);
    }

    /**
     * 更新Client信息
     * @param clientDetail 更新后的Client信息
     * @return
     */
    @PutMapping("/update")
    public IdsResponse<String, Object> updateClient(@RequestBody ClientDetail clientDetail) {
        ClientDetail detail = idsClientDetailService.update(clientDetail);
        return new IdsResponse<String, Object>().data(detail);
    }

    /**
     * 查询Client信息
     * @param clientId Client信息
     * @return
     */
    @GetMapping("/{clientId}")
    public IdsResponse<String, Object> getClientDetail(@PathVariable String clientId) {
        ClientDetail clientDetail = idsClientDetailService.getByClientId(clientId);
        return new IdsResponse<String, Object>().data(clientDetail);
    }

    /**
     * 根据id删除Client信息
     * @param id client的id
     * @return
     */
    @DeleteMapping("/removeById/{id}")
    public IdsResponse<String, Object> removeById(@PathVariable String id) {
        boolean b = idsClientDetailService.removeById(id);
        return new IdsResponse<String, Object>().data(b ? id + "删除成功" : id + "删除失败");
    }

    /**
     * 根据clientId删除Client信息
     * @param clientId client的clientId
     * @return
     */
    @DeleteMapping("/removeByClientId/{clientId}")
    public IdsResponse<String, Object> removeByClientId(@PathVariable String clientId) {
        boolean b = idsClientDetailService.removeByClientId(clientId);
        return new IdsResponse<String, Object>().data(b ? clientId + "删除成功" : clientId + "删除失败");
    }
}
