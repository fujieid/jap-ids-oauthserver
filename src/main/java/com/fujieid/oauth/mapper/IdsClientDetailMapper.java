package com.fujieid.oauth.mapper;

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
    /**
     * 通过clientId在数据库中查找对应Client
     * @param clientId clientId
     * @return ClientDetail
     */
    ClientDetail getByClientId(@Param("clientId") String clientId);

    /**
     * 注册client的详细信息获得clientId和clientSecret
     * @param clientDetail client信息
     */
    void add(ClientDetail clientDetail);

    /**
     * 对Client信息进行更新
     * @param clientDetail client信息
     */
    void update(ClientDetail clientDetail);

    /**
     * 根据id移除对应client信息
     * @param id id
     * @return 是否移除成功
     */
    boolean removeById(@Param("id") String id);

    /**
     * 根据clientId移除对应client信息
     * @param clientId clientId
     * @return 是否移除成功
     */
    boolean removeByClientId(@Param("clientId") String clientId);

    /**
     * 获取所有Client信息
     * @return 所有Client信息的链表
     */
    List<ClientDetail> getAllClientDetail();
}
