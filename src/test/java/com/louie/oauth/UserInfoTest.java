package com.louie.oauth;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.fujieid.jap.ids.model.UserInfo;
import com.louie.oauth.mapper.IdsUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lapati5
 * @date 2021/8/7
 */
@SpringBootTest
public class UserInfoTest {

    @Autowired
    private IdsUserMapper idsUserMapper;

    /**
     * 获取userInfo
     */
    @Test
    public void test01() {
        UserInfo userInfo = idsUserMapper.getById("1");
        System.out.println(userInfo.getPhone_number());
    }


}
