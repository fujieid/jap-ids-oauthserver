package com.fujieid.oauth.service;

import cn.hutool.crypto.SecureUtil;
import com.fujieid.jap.ids.model.UserInfo;
import com.fujieid.jap.ids.service.IdsClientDetailService;
import com.fujieid.jap.ids.service.IdsUserService;
import com.fujieid.oauth.mapper.IdsUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lapati5
 * @date 2021/8/4
 */
@Service
public class IdsUserServiceImpl implements IdsUserService {

    @Autowired
    private IdsUserMapper idsUserMapper;
    @Autowired
    private IdsClientDetailService idsClientDetailService;

    @Override
    public UserInfo loginByUsernameAndPassword(String username, String password, String clientId) {
        UserInfo userInfo = null;
        if (idsClientDetailService.getByClientId(clientId) != null) {
            //使用sha-256对密码进行加密取前20位与数据库中进行验证比对
            String encryptPwd = SecureUtil.sha256(password).substring(0, 20);
            userInfo =  idsUserMapper.getByNameAndPwd(username, encryptPwd);
        }
        return userInfo;
    }

    @Override
    public UserInfo getById(String userId) {
        return idsUserMapper.getById(userId);
    }

    @Override
    public UserInfo getByName(String username, String clientId) {
        UserInfo userInfo = null;
        if (idsClientDetailService.getByClientId(clientId) != null) {
            userInfo = idsUserMapper.getByName(username);
        }
        return userInfo;
    }
}
