package com.paic.vodo.node.netty.utlis;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RedisUtil implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redisTemplate=applicationContext.getBean("myredis",RedisTemplate.class);
    }
    public static RedisTemplate<String, String> redisTemplate;
    public static final String MIND_KEY = "mindsbyid:";
    public static final String CHART_KEY = "flowchartbyid:";
    /**
     * 通过id获取mind的内容
     * */
    public static String getChart(String id) {
        return redisTemplate.opsForValue().get(MIND_KEY + id);
    }
    /**
     * 通过id设置Mind内容的方法
     * */
    public static void setChart(String id, String content) {
        redisTemplate.opsForValue().set(MIND_KEY + id, content);
    }

    /**
     * 通过id获取mind的内容
     * */
    public static String getMind(String id) {
        return redisTemplate.opsForValue().get(MIND_KEY + id);
    }
    /**
     * 通过id设置Mind内容的方法
     * */
    public static void setMind(String id, String content) {
        redisTemplate.opsForValue().set(MIND_KEY + id, content);
    }
    /**
     * 共享文件时设置的参数
     */
    public static void setShare(String a, String b) {
        redisTemplate.opsForValue().set(a, b, 1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(b, a, 1, TimeUnit.DAYS);
    }
    public static void deleteShare(String uuid) {
        delete(redisTemplate.opsForValue().get(uuid));
        delete(uuid);
    }
    /*****************************************************************************************/

                        /****************************基本方法************************/

    /*****************************************************************************************/

    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public  static  void set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public  static void set(String key,String value,Long time){
        redisTemplate.opsForValue().set(key,value,time,TimeUnit.MINUTES);
    }

    public static void delete(String key){
        redisTemplate.delete(key);
    }



}
