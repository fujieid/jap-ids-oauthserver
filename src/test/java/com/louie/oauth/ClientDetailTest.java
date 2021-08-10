package com.louie.oauth;

import com.fujieid.jap.ids.model.ClientDetail;
import com.louie.oauth.mapper.IdsClientDetailMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.function.Function;

/**
 * @author lapati5
 * @date 2021/8/6
 */
@SpringBootTest
public class ClientDetailTest {

    @Autowired
    private IdsClientDetailMapper mapper;

    /**
     * addClient
     */
    @Test
    public void test01() {
        ClientDetail clientDetail = new ClientDetail()
                .setId("1")
                .setAppName("app")
                .setClientId("123")
                .setClientSecret("123")
                .setRedirectUri("http://localhost");
        System.out.println(clientDetail);
        mapper.add(clientDetail);
    }
    /**
     * getClient
     */
    @Test
    public void test02() {
        System.out.println("ClientId(\"123\") = " + mapper.getByClientId("123"));
    }

    /**
     * getAll
     */
    @Test
    public void test03() {
        mapper.getAllClientDetail().forEach(clientDetail -> {
            System.out.println(clientDetail.getAppName());
        });
    }

    /**
     * removeById
     */
    @Test
    public void test04() {
//        boolean flag = mapper.removeById("1");
        boolean flag = mapper.removeByClientId("123");
        System.out.println(flag);
    }
}
