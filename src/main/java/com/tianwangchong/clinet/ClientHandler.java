package com.tianwangchong.clinet;

import com.tianwangchong.protocol.Packet;
import com.tianwangchong.protocol.PacketCodeC;
import com.tianwangchong.protocol.request.LoginRequestPacket;
import com.tianwangchong.protocol.response.LoginResponsePacket;
import com.tianwangchong.protocol.response.MessageResponsePacket;
import com.tianwangchong.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

/**
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 连接上服务端之后，Netty 会回调到 ClientHandler 的 channelActive() 方法
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        // 编码
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);

        // 写数据(通过 ctx.channel() 获取到当前连接（Netty 对连接的抽象为 Channel），然后调用 writeAndFlush() 就能把二进制数据写到服务端。)
        ctx.channel().writeAndFlush(buffer);
    }

    /**
     * 客户端接收服务端数据
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            // 登录响应数据包
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
                // 客户端登录成功之后，给客户端绑定登录成功的标志位
                LoginUtil.markAsLogin(ctx.channel());
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        } else if (packet instanceof MessageResponsePacket) {
            // 消息回复响应数据包
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }
    }
}
