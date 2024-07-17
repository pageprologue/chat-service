package com.anchoreer.chat.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class RedisConfig {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory("localhost", 6379)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory)
    }
}
