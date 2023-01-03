package com.tianwangchong.server.handler;

import com.tianwangchong.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 是否登录处理器
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class AuthHandler extends ChannelInboundHandlerAdapter {

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
