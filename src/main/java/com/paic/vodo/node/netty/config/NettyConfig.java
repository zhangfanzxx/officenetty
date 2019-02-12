package com.paic.vodo.node.netty.config;

import com.paic.vodo.node.netty.socket.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @BelongsProject: netty
 * @BelongsPackage: com.paic.vodo.node.netty.config
 * @Author: zff
 * @CreateTime: 2018-12-31 12:54
 * @Description: ${Description}
 */

@Slf4j
@Configuration
public class NettyConfig {

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    @Value("${netty.port}")
    private int port;

    @PreDestroy
    public void close() {
        log.info("关闭服务器....");
        //优雅退出
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Autowired
    WebSocketChannelInitializer webSocketChannelInitializer;

    @PostConstruct
    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }


    public void start() {
        try {
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(webSocketChannelInitializer);
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
            log.info("netty启动完成");
        } catch (Exception e) {
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
