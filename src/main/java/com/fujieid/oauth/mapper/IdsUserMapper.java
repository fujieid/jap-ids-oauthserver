package com.fujieid.oauth.mapper;

import com.fujieid.jap.ids.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author lapati5
 * @date 2021/8/6
 */
@Mapper
@Repository
public interface IdsUserMapper {

    /**
     * 获取资源服务其中用户的信息
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfo getById(@Param("userId") String userId);

    /**
     * 根据用户名称在资源服务器中获取用户信息
     * @param username 用户名
     * @param clientId 提出申请的clientId
     * @return 用户信息
     */
    UserInfo getByName(@Param("username") String username);

    /**
     * 验证用户名和密码是否匹配
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    UserInfo getByNameAndPwd(@Param("username") String username,
                             @Param("password") String password);

}
