package com.aztech.cds.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@ConditionalOnProperty(
        name = "spring.cache.type",
        havingValue = "redis"
)
@Getter
@Slf4j
public class RedisConfig {

    @Autowired
    private RedisConfigReader redisConfigReader;

    public RedisStandaloneConfiguration getRedisStandaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisConfigReader.getHost(), redisConfigReader.getPort());
//        configuration.setPassword(redisConfigReader.getPassword());

        /// TODO :: Add password to the configuration once it on production
        return configuration;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(
                        ClientOptions.builder()
                                .socketOptions(
                                        SocketOptions.builder()
                                                .connectTimeout(Duration.ofMillis(redisConfigReader.getSocketTimeoutInMillis()))
                                                .build()
                                ).build())
                .commandTimeout(Duration.ofSeconds(redisConfigReader.getCommandTimeoutInSeconds()))
                .build();
        return new LettuceConnectionFactory(getRedisStandaloneConfiguration(), clientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
//        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
//        template.setKeySerializer(new StringRedisSerializer());

        // Serializer for values (to handle objects)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(redisConfigReader.getExpiryInSeconds()))
                .disableCachingNullValues();
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration())
                .transactionAware().build();
    }

    @Bean
    public RedisClient redisClient() {
        RedisURI redisURI = RedisURI.Builder.redis(redisConfigReader.getHost(), redisConfigReader.getPort())
//                .withPassword(redisConfigReader.getPassword())
                .build();
        return RedisClient.create(redisURI);
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection() {
        return redisClient().connect();
    }
}

