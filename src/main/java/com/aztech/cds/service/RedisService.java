package com.aztech.cds.service;

public interface RedisService {


    public boolean exists(String key);

    public boolean saveIfAbsent(String key, Object value, long timeoutInSeconds);

    public <T> T getIfExists(String key, Class<T> clazz);

    public void delete(String key);
}
