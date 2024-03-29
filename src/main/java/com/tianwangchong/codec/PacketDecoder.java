package com.tianwangchong.codec;

import com.tianwangchong.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码
 *
 * 当我们继承了 ByteToMessageDecoder 这个类之后，我们只需要实现一下 decode() 方法，这里的 in 大家可以看到，传递进来的时候就已经是 ByteBuf 类型，所以我们不再需要强转，第三个参数是 List 类型，我们通过往这个 List 里面添加解码后的结果对象，就可以自动实现结果往下一个 handler 进行传递，这样，我们就实现了解码的逻辑 handler。
 *
 * 使用 ByteToMessageDecoder，Netty 会自动进行内存的释放
 *
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}
