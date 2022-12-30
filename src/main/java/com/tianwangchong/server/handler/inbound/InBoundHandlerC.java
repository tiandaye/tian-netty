package com.tianwangchong.server.handler.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class InBoundHandlerC extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InBoundHandlerC: " + msg);

        // super.channelRead(ctx, msg);

        ctx.channel().writeAndFlush(msg);
        // super.channelRead(ctx, msg);修改为 ctx.channel().writeAndFlush(msg);即可略过所有的inBound而直接传递到第一个OutBoundHandler
    }
}
