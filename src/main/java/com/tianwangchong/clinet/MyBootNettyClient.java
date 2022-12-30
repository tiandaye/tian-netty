package com.tianwangchong.clinet;

import com.tianwangchong.clinet.handler.MyBootNettyClientChannelInboundHandlerAdapter;
import com.tianwangchong.codec.MyDecoder;
import com.tianwangchong.codec.MyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 私有协议-客户端
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class MyBootNettyClient {
    public static void main(String[] args) {
        new MyBootNettyClient().connect("192.168.3.201", 6000);
    }

    public void connect(String host, int port) {
        /**
         * 客户端的NIO线程组
         *
         */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /**
             * Bootstrap 是一个启动NIO服务的辅助启动类 客户端的
             */
            Bootstrap bootstrap = new Bootstrap();

            /**
             * 设置group
             */
            bootstrap = bootstrap.group(group);

            /**
             * 关联客户端通道
             */
            bootstrap = bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);

            /**
             * 设置 I/O处理类,主要用于网络I/O事件，记录日志，编码、解码消息
             */
            bootstrap = bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    // 添加自定义协议的编解码工具
                    pipeline.addLast(new MyDecoder());
                    pipeline.addLast(new MyEncoder());
                    /**
                     * 自定义ChannelInboundHandlerAdapter
                     */
                    pipeline.addLast(new MyBootNettyClientChannelInboundHandlerAdapter());
                }
            });

            /**
             * 连接服务端
             */
            ChannelFuture f = bootstrap.connect(host, port).sync();
            System.out.println(("TCP客户端连接成功, 地址是： " + host + ":" + port));

            /**
             * 等待连接端口关闭
             */
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(("启动netty client失败：" + e));
        } finally {
            /**
             * 退出，释放资源
             */
            group.shutdownGracefully();
        }
    }
}
