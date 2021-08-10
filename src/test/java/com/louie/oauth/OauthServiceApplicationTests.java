package com.louie.oauth;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.fujieid.jap.ids.model.enums.TokenSigningAlg;
import com.fujieid.jap.ids.util.JwkUtil;
import com.louie.oauth.utils.JapIdsConstUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.LocalDateTime;

@SpringBootTest
class OauthServiceApplicationTests {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Test
    void contextLoads() {

    }

    /**
     * jwk json encryption
     */
    @Test
    public void test01() {
        String keySetJson = JwkUtil.createRsaJsonWebKeySetJson("jap-jwk-keyid", TokenSigningAlg.RS256);
        System.out.println(keySetJson);
    }


    /**
     * hutool
     */
    @Test
    public void test02() {
        System.out.println(IdUtil.fastUUID());
        System.out.println(IdUtil.simpleUUID());
        Snowflake snowflake = IdUtil.createSnowflake(11, 11);
        System.out.println(snowflake.nextId());
    }

    /**
     * json localDateTime
     */
    @Test
    public void test03() {
        redisTemplate.opsForValue().set("k1", LocalDateTime.now());
    }

    /**
     * sha256
     */
    @Test
    public void test04() {
        System.out.println(SecureUtil.sha256("12345").substring(0, 20));
    }

    /**
     * model and view
     */
    @Test
    public void test05() {
        System.out.println("JapIdsProperties.host = " + JapIdsConstUtil.HOST);
        System.out.println(JapIdsConstUtil.LOGIN_PAGE.length());
    }
}
