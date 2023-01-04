package com.tianwangchong.server.handler;

import com.tianwangchong.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 是否登录处理器
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

// 1. 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    // 2. 构造单例
    public static final AuthHandler INSTANCE = new AuthHandler();

    private AuthHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!SessionUtil.hasLogin(ctx.channel())) {
            // 未登录直接关闭
            ctx.channel().close();
        } else {
            // 如果登录了, 从pipeline中移除
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
}
