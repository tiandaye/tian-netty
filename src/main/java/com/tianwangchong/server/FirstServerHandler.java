package com.tianwangchong.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author: tianwangchong
 * @date: 2020/11/2 4:36 下午
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 这个方法里面的逻辑和客户端侧类似，获取服务端侧关于这条连接的逻辑处理链 pipeline，然后添加一个逻辑处理器，负责读取客户端发来的数据
	 * <p>
	 * 服务端侧的逻辑处理器同样继承自 ChannelInboundHandlerAdapter，与客户端不同的是，这里覆盖的方法是 channelRead()，这个方法在接收到客户端发来的数据之后被回调。
	 * <p>
	 * 这里的 msg 参数指的就是 Netty 里面数据读写的载体，为什么这里不直接是 ByteBuf，而需要我们强转一下，我们后面会分析到。这里我们强转之后，然后调用 byteBuf.toString() 就能够拿到我们客户端发过来的字符串数据。
	 *
	 * @param ctx
	 * @param msg
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		/**
		 * 收到客户端数据
		 */
		ByteBuf byteBuf = (ByteBuf) msg;

		System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));

		/**
		 * 回复数据到客户端
		 */
		System.out.println(new Date() + ": 服务端写出数据");
		ByteBuf out = getByteBuf(ctx);
		ctx.channel().writeAndFlush(out);
	}

	private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
		byte[] bytes = "你好，欢迎关注tiandaye!".getBytes(Charset.forName("utf-8"));

		ByteBuf buffer = ctx.alloc().buffer();

		buffer.writeBytes(bytes);

		return buffer;
	}
}
