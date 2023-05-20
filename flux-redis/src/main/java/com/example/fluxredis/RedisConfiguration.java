package com.example.fluxredis;


import org.redisson.Redisson;
import org.redisson.spring.cache.RedissonCacheStatisticsAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Autowired
    private Redisson redisson;

}
