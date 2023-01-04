package com.tianwangchong.clinet;

import com.tianwangchong.clinet.console.ConsoleCommandManager;
import com.tianwangchong.clinet.console.LoginConsoleCommand;
import com.tianwangchong.clinet.handler.CreateGroupResponseHandler;
import com.tianwangchong.clinet.handler.GroupMessageResponseHandler;
import com.tianwangchong.clinet.handler.JoinGroupResponseHandler;
import com.tianwangchong.clinet.handler.ListGroupMembersResponseHandler;
import com.tianwangchong.clinet.handler.LoginResponseHandler;
import com.tianwangchong.clinet.handler.LogoutResponseHandler;
import com.tianwangchong.clinet.handler.MessageResponseHandler;
import com.tianwangchong.clinet.handler.QuitGroupResponseHandler;
import com.tianwangchong.codec.PacketDecoder;
import com.tianwangchong.codec.PacketEncoder;
import com.tianwangchong.codec.Spliter;
import com.tianwangchong.protocol.request.LoginRequestPacket;
import com.tianwangchong.protocol.request.MessageRequestPacket;
import com.tianwangchong.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Netty 客户端
 *
 * @author: tianwangchong
 * @date: 2020/11/2 2:45 下午
 */
public class NettyClient {

    /**
     * 最大尝试次数
     */
    private static final int MAX_RETRY = 5;

    /**
     * ip
     */
    private static final String HOST = "127.0.0.1";

    /**
     * 端口
     */
    private static final int PORT = 8000;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 客户端启动的引导类是 Bootstrap，负责启动客户端以及连接服务端，而上一小节我们在描述服务端的启动的时候，这个辅导类是 ServerBootstrap
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                /**
                 * 客户端相关的数据读写逻辑是通过 Bootstrap 的 handler() 方法指定
                 */
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        /**
                         * ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式
                         * 然后再调用 addLast() 方法添加一个逻辑处理器，这个逻辑处理器为的就是在客户端建立连接成功之后，向服务端写数据，下面是这个逻辑处理器相关的代码
                         */
                        // ch.pipeline().addLast(new FirstClientHandler());

                        /**
                         * 使用 ClientHandler 来走登录例子
                         */
                        // ch.pipeline().addLast(new ClientHandler());

                        /**
                         * ByteToMessageDecoder
                         * SimpleChannelInboundHandler
                         * MessageToByteEncoder
                         */
                        // 基于长度域拆包器
                        // ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        // 登录响应处理器
                        ch.pipeline().addLast(new LoginResponseHandler());
                        // 收消息处理器
                        ch.pipeline().addLast(new MessageResponseHandler());
                        // 创建群响应处理器
                        ch.pipeline().addLast(new CreateGroupResponseHandler());
                        // 加群响应处理器
                        ch.pipeline().addLast(new JoinGroupResponseHandler());
                        // 退群响应处理器
                        ch.pipeline().addLast(new QuitGroupResponseHandler());
                        // 获取群成员响应处理器
                        ch.pipeline().addLast(new ListGroupMembersResponseHandler());
                        // 群消息响应
                        ch.pipeline().addLast(new GroupMessageResponseHandler());
                        // 登出响应处理器
                        ch.pipeline().addLast(new LogoutResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());

                        /**
                         * StringEncoder
                         */
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
                // System.out.println("连接成功!");
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                // 连接成功之后，启动控制台线程
                startConsoleThread(channel);
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

    /**
     * 开启控制台线程
     *
     * @param channel
     */
    private static void startConsoleThread(Channel channel) {
        // 管理控制台命令执行器
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        // 登录控制台命令
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        // 输入
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    loginConsoleCommand.exec(scanner, channel);
                } else {
                    consoleCommandManager.exec(scanner, channel);
                }
            }
        }).start();
    }

    /**
     * 开启控制台线程 - 老代码, 没重构之前
     *
     * @param channel
     */
    private static void startConsoleThread2(Channel channel) {
        /**
         * 为什么要在这里新创建一个线程来进行接收控制台输入的数据和发送给服务端呢？
         * 解答：
         * 因为调用本办法是在监听中，而监听是用来监视客户端是否与服务端连接上了的
         * 监听会一直不间断的获取结果，一旦连接成功了，就会调用本方法来打开控制台接收数据
         * 可是此时还没有执行到ClientHandler的channelActive方法，也就表示还没有进行登录操作
         * 所以如果我们在本方法中直接使用while循环的话，就会造成主线程堵塞在这里，无法继续执行
         * ClientHandler的channelActive方法，而没有登录也就导致LoginUtil.hasLogin(channel)
         * 这个判断一直为false，所以造成了无限死循环
         */
        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        new Thread(() -> {
            while (!Thread.interrupted()) {
                // 判断只要当前 channel 是登录状态，就允许控制台输入消息, 没登录则输入用户名登录
                if (!SessionUtil.hasLogin(channel)) {
                    // 如果当前用户还未登录，我们在控制台输入一个用户名，然后构造一个登录数据包发送给服务器，发完之后，我们等待一个超时时间，可以当做是登录逻辑的最大处理时间，这里就简单粗暴点了
                    System.out.print("输入用户名登录: ");
                    String username = sc.nextLine();
                    loginRequestPacket.setUsername(username);

                    // 密码使用默认的
                    loginRequestPacket.setPassword("pwd");

                    // 发送登录数据包
                    channel.writeAndFlush(loginRequestPacket);

                    waitForLoginResponse();
                } else {
                    // 如果当前用户已经是登录状态，我们可以在控制台输入消息接收方的 userId，然后输入一个空格，再输入消息的具体内容，然后，我们就可以构建一个消息数据包，发送到服务端。
                    // 发送给那个用户
                    String toUserId = sc.next();
                    // 消息
                    String message = sc.next();
                    channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
