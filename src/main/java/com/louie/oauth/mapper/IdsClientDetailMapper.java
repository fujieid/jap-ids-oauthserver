package com.louie.oauth.mapper;

import com.fujieid.jap.ids.model.ClientDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lapati5
 * @date 2021/8/6
 */
@Mapper
@Repository
public interface IdsClientDetailMapper {
    ClientDetail getByClientId(@Param("clientId") String clientId);

    /**
     * 注册client的详细信息获得clientId和clientSecret
     * @param clientDetail client信息
     */
    void add(ClientDetail clientDetail);

    void update(ClientDetail clientDetail);

    boolean removeById(@Param("id") String id);

    boolean removeByClientId(@Param("clientId") String clientId);

    List<ClientDetail> getAllClientDetail();
}
