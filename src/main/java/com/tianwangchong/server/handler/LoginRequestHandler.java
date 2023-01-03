package com.tianwangchong.server.handler;

import com.tianwangchong.protocol.request.LoginRequestPacket;
import com.tianwangchong.protocol.response.LoginResponsePacket;
import com.tianwangchong.session.Session;
import com.tianwangchong.util.IDUtil;
import com.tianwangchong.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 处理登录请求处理器
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    /**
     * 1. 第二个参数如何来的?
     * <p>
     * 接收到请求后在ByteToMessageDecoder类中的channelRead()最终会调用到自定义的PacketDecoder#decode方法，把bytebuf转换成XXXRequestPacket对象之后放在out容器中，后续会遍历该容器，取出对象，依次执行SimpleChannelInboundHandler类中的channelRead0(channel, 对象类型)的方法，该方法是个抽象方法，会自动从子类中寻找对应的子类方法，就找到了我们的LoginRequestHandler的channelRead0()方法，并把out容器中的该对象的引用传递过来
     * <p>
     * 源码里面,遍历out容器后执行ctx的fireChannelRead,调用SimpleChannelInboundHandler父类ChannelRead的实现,然后再调用到channelRead0
     * <p>
     * 2. 怎么判断bytebuf是堆外内存？
     * ByteBuf.isDirect()
     *
     * @param ctx
     * @param loginRequestPacket
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUsername());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            String userId = IDUtil.randomId();
            loginResponsePacket.setUserId(userId);
            System.out.println("[" + loginRequestPacket.getUsername() + "]登录成功");
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUsername()), ctx.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);

        /**
         * 老代码
         */
        // System.out.println(new Date() + ": 收到客户端登录请求……");
        //
        // LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        // loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        // if (valid(loginRequestPacket)) {
        //     loginResponsePacket.setSuccess(true);
        //     System.out.println(new Date() + ": 登录成功!");
        // } else {
        //     loginResponsePacket.setReason("账号密码校验失败");
        //     loginResponsePacket.setSuccess(false);
        //     System.out.println(new Date() + ": 登录失败!");
        // }
        //
        // // 登录响应
        // ctx.channel().writeAndFlush(loginResponsePacket);
    }

    // private boolean valid(LoginRequestPacket loginRequestPacket) {
    //     return true;
    // }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    /**
     * 用户断线之后取消绑定
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
