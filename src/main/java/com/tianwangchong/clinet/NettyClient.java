package com.tianwangchong.clinet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Netty 客户端
 *
 * @author: tianwangchong
 * @date: 2020/11/2 2:45 下午
 */
public class NettyClient {

	private static final int MAX_RETRY = 5;

	private static final String HOST = "127.0.0.1";

	private static final int PORT = 8000;

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();

		// 客户端启动的引导类是 Bootstrap，负责启动客户端以及连接服务端，而上一小节我们在描述服务端的启动的时候，这个辅导类是 ServerBootstrap
		Bootstrap bootstrap = new Bootstrap();
		bootstrap
				// 1.指定线程模型
				.group(group)
				// 2.指定 IO 类型为 NIO
				.channel(NioSocketChannel.class)
				// 3.IO 处理逻辑
				/**
				 * 客户端相关的数据读写逻辑是通过 Bootstrap 的 handler() 方法指定
				 */
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						// ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式
						// 然后再调用 addLast() 方法 添加一个逻辑处理器，这个逻辑处理器为的就是在客户端建立连接成功之后，向服务端写数据，下面是这个逻辑处理器相关的代码
						ch.pipeline().addLast(new FirstClientHandler());

						// ch.pipeline().addLast(new StringEncoder());
					}
				});

		/**
		 * attr() 方法
		 *
		 * attr() 方法可以给客户端 Channel，也就是NioSocketChannel绑定自定义属性，然后我们可以通过channel.attr()取出这个属性，比如，上面的代码我们指定我们客户端 Channel 的一个clientName属性，属性值为nettyClient，其实说白了就是给NioSocketChannel维护一个 map 而已，后续在这个 NioSocketChannel 通过参数传来传去的时候，就可以通过他来取出设置的属性，非常方便。
		 */
		bootstrap.attr(AttributeKey.newInstance("clientName"), "nettyClient");

		/**
		 * option() 方法
		 * option() 方法可以给连接设置一些 TCP 底层相关的属性，比如上面，我们设置了三种 TCP 属性，其中
		 *
		 * ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
		 * ChannelOption.SO_KEEPALIVE 表示是否开启 TCP 底层心跳机制，true 为开启
		 * ChannelOption.TCP_NODELAY 表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就设置为 true 关闭，如果需要减少发送次数减少网络交互，就设置为 false 开启
		 */
		bootstrap
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true);

		// // 4.建立连接
		// bootstrap.connect("juejin.im", 80).addListener(future -> {
		// 	if (future.isSuccess()) {
		// 		System.out.println("连接成功!");
		// 	} else {
		// 		System.err.println("连接失败!");
		// 	}
		//
		// });

		// // 4.建立连接
		connect(bootstrap, HOST, PORT, MAX_RETRY);
		//
		// Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
		//
		// while (true) {
		// 	channel.writeAndFlush(new Date() + ": hello world!");
		// 	Thread.sleep(2000);
		// }
	}

	/**
	 * 带重试功能的
	 *
	 * @param bootstrap
	 * @param host
	 * @param port
	 * @param retry
	 */
	private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
		bootstrap.connect(host, port).addListener(future -> {
			if (future.isSuccess()) {
				System.out.println("连接成功!");
			} else if (retry == 0) {
				System.err.println("重试次数已用完，放弃连接！");
			} else {
				// 第几次重连
				int order = (MAX_RETRY - retry) + 1;
				// 本次重连的间隔
				int delay = 1 << order;
				System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");

				// 定时任务是调用 bootstrap.config().group().schedule(), 其中 bootstrap.config() 这个方法返回的是 BootstrapConfig，他是对 Bootstrap 配置参数的抽象，然后 bootstrap.config().group() 返回的就是我们在一开始的时候配置的线程模型 workerGroup，调 workerGroup 的 schedule 方法即可实现定时任务逻辑。
				bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
						.SECONDS);
			}
		});
	}

	/**
	 * 连接, 连接失败则重试
	 *
	 * @param bootstrap
	 * @param host
	 * @param port
	 */
	private static void connect(Bootstrap bootstrap, String host, int port) {
		bootstrap.connect(host, port).addListener(future -> {
			if (future.isSuccess()) {
				System.out.println("连接成功!");
			} else {
				System.err.println("连接失败，开始重连");
				connect(bootstrap, host, port);
			}
		});
	}
}