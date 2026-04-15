package com.edigest.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Disabled
    @Test
    void testSendMail(){
        redisTemplate.opsForValue().set("email","riteshpeepal@gmail.com");
        Object email = redisTemplate.opsForValue().get("email");
        System.out.println("Email from Redis: " + email);
        int a = 1;
    }
}
