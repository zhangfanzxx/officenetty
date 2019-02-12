package com.paic.vodo.node.netty.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @BelongsProject: netty
 * @BelongsPackage: com.paic.vodo.node.netty.config
 * @Author: zff
 * @CreateTime: 2019-01-01 15:38
 * @Description: ${Description}
 */
@Configuration
public class RedisConfig {

    @Bean(name="myredis")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name="myredis2")
    public RedisTemplate redisTemplate2(RedisConnectionFactory factory) {

        StringRedisTemplate template = new StringRedisTemplate(factory);
        ByteRedisSerializer byteRedisSerializer=new ByteRedisSerializer();
        template.setValueSerializer(byteRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

@Bean
    public RedisSessionDAO getRdis(){
        RedisSessionDAO dao=new RedisSessionDAO();

        return dao;
}


}
