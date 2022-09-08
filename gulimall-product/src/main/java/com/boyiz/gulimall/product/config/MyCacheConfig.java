package com.boyiz.gulimall.product.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class MyCacheConfig {

    /**
     * 会导致配置文件中的设置失效
     * @return
     */
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        config = config.entryTtl(Duration.ofMillis(300000));//设置失效时间
        config = config.computePrefixWith(CacheKeyPrefix.prefixed("CACHE_"));//设置前缀
        //默认缓存空值
        return config;
    }
}
