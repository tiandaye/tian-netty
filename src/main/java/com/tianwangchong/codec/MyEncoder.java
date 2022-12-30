package com.tianwangchong.codec;

import com.tianwangchong.protocol.MyProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 私有协议-编码器
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class MyEncoder extends MessageToByteEncoder<MyProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MyProtocol myProtocol, ByteBuf out) throws Exception {
        // 1.写入消息的开头的信息标志(CYCR)
        out.writeBytes(myProtocol.getHead().getBytes());
        // 2.写入消息的长度(负载长度)
        out.writeShort(myProtocol.getContentLength() - 4);
        // 3.写入消息的内容(byte[]类型)
        out.writeBytes(myProtocol.getContent());
    }
}