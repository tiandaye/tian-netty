package com.tianwangchong.clinet.handler;

import com.tianwangchong.protocol.MyProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 私有协议-自定义ChannelInboundHandlerAdapter
 *
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class MyBootNettyClientChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    public MyBootNettyClientChannelInboundHandlerAdapter() {

    }

    /**
     * 从服务端收到新的数据时，这个方法会在收到消息时被调用
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyProtocol protocol = (MyProtocol) msg;
        System.out.println(("接收到服务端的消息:" + protocol));
    }

    /**
     * 从服务端收到新的数据、读取完成时调用
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
        ctx.flush();
    }

    /**
     * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws InterruptedException {
        System.out.println(("exceptionCaught：" + cause.getMessage()));
        ctx.close();//抛出异常，断开与客户端的连接
    }

    /**
     * 客户端与服务端第一次建立连接时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        System.out.println(("channelActive------TCP客户端新建连接------clientIp:" + clientIp));
    }

    /**
     * 客户端与服务端 断连时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        ctx.close(); // 断开连接时，必须关闭，否则造成资源浪费
        System.out.println(("channelInactive------TCP客户端断开连接----------clientIp:" + clientIp));
    }
}