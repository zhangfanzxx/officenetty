package com.paic.vodo.node.netty.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * @BelongsProject: netty
 * @BelongsPackage: com.paic.vodo.node.netty.config
 * @Author: zff
 * @CreateTime: 2019-01-03 16:40
 * @Description: ${Description}
 */
public class ByteRedisSerializer implements RedisSerializer<byte[]> {
    private final Charset charset;

    public ByteRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public ByteRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(byte[] t) throws SerializationException {
        return t;
    }

    @Override
    public byte[] deserialize(byte[] bytes) throws SerializationException {
        return bytes;
    }

}