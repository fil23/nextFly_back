package com.nextfly.demo.configuration.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private static final String CODICE = "CODICE";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, timeout, unit);
    }

    public void saveNoExp(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveCodice(String key, String value) {
        redisTemplate.opsForHash().put(CODICE, key, value);
        redisTemplate.expire(CODICE, 15, TimeUnit.MINUTES);
    }

    public String getCodiciEntity(String email) {
        return (String) redisTemplate.opsForHash().get(CODICE, email);
    }

    public void deleteCodice(String email) {
        redisTemplate.opsForHash().delete(CODICE, email);
    }

    public Object find(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
