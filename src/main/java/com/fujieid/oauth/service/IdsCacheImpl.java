package com.fujieid.oauth.service;

import com.fujieid.jap.core.cache.JapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author lapati5
 * @date 2021/8/5
 */
@Component
public class IdsCacheImpl implements JapCache {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void set(String s, Serializable serializable) {
        redisTemplate.opsForValue().set(s, serializable);
    }

    @Override
    public void set(String s, Serializable serializable, long l) {
        redisTemplate.opsForValue().set(s, serializable, l);
    }

    @Override
    public Serializable get(String s) {
        return redisTemplate.opsForValue().get(s);
    }

    @Override
    public boolean containsKey(String s) {
        return redisTemplate.hasKey(s);
    }

    @Override
    public void removeKey(String s) {
        redisTemplate.delete(s);
    }
}
