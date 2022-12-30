package com.tianwangchong.codec;

import com.tianwangchong.protocol.MyProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 私有协议-解码器
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class MyDecoder extends ByteToMessageDecoder {
    /**
     * <pre>
     * 协议开始的标准head_data，CYRC，占据4个字节.
     * 表示数据的长度contentLength，占据2个字节.
     * </pre>
     */
    public final int BASE_LENGTH = 10;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.readableBytes() >= BASE_LENGTH) {
            if (buffer.readableBytes() > 2048) {
                buffer.skipBytes(buffer.readableBytes());
            }

            // 记录包头开始的index
            int beginReader;
            // CYRC 43 59 52 43
            while (true) {
                // 获取包头开始的index
                beginReader = buffer.readerIndex();
                // 标记包头开始的index
                buffer.markReaderIndex();
                // 读到了协议的开始标志，结束while循环
                int head1 = buffer.readUnsignedShort();
                int head2 = buffer.readUnsignedShort();
                if (head1 == 17241 && head2 == 21059) {
                    break;
                }

                // 未读到包头，略过一个字节
                // 每次略过，一个字节，去读取，包头信息的开始标记
                buffer.resetReaderIndex();
                buffer.readByte();
                // 当略过，一个字节之后，数据包的长度，又变得不满足
                // 此时，应该结束。等待后面的数据到达
                if (buffer.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }

            // 消息的长度
            int length = buffer.readUnsignedShort() + 4;
            // 判断请求数据包数据是否到齐
            if (buffer.readableBytes() < length) {
                // 还原读指针
                buffer.readerIndex(beginReader);
                return;
            }

            // 读取data数据
            byte[] data = new byte[length];
            buffer.readBytes(data);

            MyProtocol protocol = new MyProtocol(data.length, data);
            out.add(protocol);
        }
    }
}
