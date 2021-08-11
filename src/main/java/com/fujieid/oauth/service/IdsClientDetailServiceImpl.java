package com.fujieid.oauth.service;

import com.fujieid.jap.ids.model.ClientDetail;
import com.fujieid.jap.ids.model.enums.GrantType;
import com.fujieid.jap.ids.model.enums.ResponseType;
import com.fujieid.jap.ids.provider.IdsScopeProvider;
import com.fujieid.jap.ids.service.IdsClientDetailService;
import com.fujieid.jap.ids.util.OauthUtil;
import com.fujieid.oauth.mapper.IdsClientDetailMapper;
import com.xkcoding.http.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author lapati5
 * @date 2021/8/4
 */
@Service
public class IdsClientDetailServiceImpl implements IdsClientDetailService {

    @Autowired
    private IdsClientDetailMapper idsClientDetailMapper;

    @Override
    public ClientDetail getByClientId(String clientId) {
        return idsClientDetailMapper.getByClientId(clientId);
    }

    @Override
    public ClientDetail add(ClientDetail clientDetail) {
        //生成clientId和ClientSecret
        clientDetail.setId(UUID.randomUUID().toString().replace("-", ""))
                    .setClientId(OauthUtil.generateClientId())
                    .setClientSecret(OauthUtil.generateClientSecret())
                    .setAvailable(true);
        //默认允许获取所有信息
        if (StringUtil.isEmpty(clientDetail.getScopes()) || clientDetail.getScopes().length() == 0) {
            clientDetail.setScopes(String.join(" ", IdsScopeProvider.getScopeCodes()));
        }
        //默认适用所有授权模式
        if (StringUtil.isEmpty(clientDetail.getGrantTypes()) || StringUtil.isEmpty(clientDetail.getResponseTypes())) {
            clientDetail.setGrantTypes(String.join(" ", GrantType.grantTypes()));
            clientDetail.setResponseTypes(String.join(" ", Arrays.asList(ResponseType.CODE.getType(), ResponseType.TOKEN.getType(), ResponseType.ID_TOKEN.getType(), ResponseType.NONE.getType())));
        }
        //存入数据库中
        idsClientDetailMapper.add(clientDetail);

        System.out.println("client detail: " + clientDetail.getAppName() + ", " + clientDetail.getClientId());
        return clientDetail;
    }

    @Override
    public ClientDetail update(ClientDetail clientDetail) {
        idsClientDetailMapper.update(clientDetail);
        return clientDetail;
    }

    @Override
    public boolean removeById(String id) {
        return idsClientDetailMapper.removeById(id);
    }

    @Override
    public boolean removeByClientId(String clientId) {
        return idsClientDetailMapper.removeByClientId(clientId);
    }

    @Override
    public List<ClientDetail> getAllClientDetail() {
        return idsClientDetailMapper.getAllClientDetail();
    }
}
