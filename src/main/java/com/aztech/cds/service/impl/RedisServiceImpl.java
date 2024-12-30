package com.aztech.cds.service.impl;

import com.aztech.cds.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    // Check if a key exists
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // Save an object if the key doesn't already exist
    public boolean saveIfAbsent(String key, Object value, long timeoutInSeconds) {
        if (!exists(key)) {
            redisTemplate.opsForValue().set(key, value, timeoutInSeconds, TimeUnit.SECONDS);
            return true; // Successfully saved
        }
        return false; // Key already exists
    }

    // Retrieve an object if the key exists
    public <T> T getIfExists(String key, Class<T> clazz) {
        if (!exists(key)) {
            return null; // Key does not exist
        }
        Object value = redisTemplate.opsForValue().get(key);
        return clazz.cast(value);
    }

    // Delete a key
    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
