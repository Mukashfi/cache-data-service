package com.aztech.cds.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.cache.redis")
@Getter
@Setter
public class RedisConfigReader {

    private String host;
    private Integer port;
    private String password;
    private Integer expiryInSeconds;
    private Integer commandTimeoutInSeconds;
    private Integer socketTimeoutInMillis = 2000;


}
