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

    UserInfo getById(@Param("userId") String userId);

    UserInfo getByName(@Param("username") String username, @Param("clientId") String clientId);

    UserInfo getByNameAndPwd(@Param("username") String username, @Param("password") String password);

}
