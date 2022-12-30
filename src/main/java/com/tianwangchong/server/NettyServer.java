package com.tianwangchong.server;

import com.tianwangchong.codec.PacketDecoder;
import com.tianwangchong.codec.PacketEncoder;
import com.tianwangchong.codec.Spliter;
import com.tianwangchong.server.handler.LoginRequestHandler;
import com.tianwangchong.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Netty 服务端
 * <p>
 * 将 Netty 里面的概念与 IO 模型结合起来可能更好理解
 * <p>
 * 1. boss 对应 IOServer.java 中的接受新连接线程，主要负责创建新连接
 * 2. worker 对应 IOServer.java 中的负责读取数据的线程，主要用于读取数据以及业务逻辑处理
 *
 * @author: tianwangchong
 * @date: 2020/11/2 2:42 下午
 */
public class NettyServer {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        /**
         * 首先看到，我们创建了两个NioEventLoopGroup，这两个对象可以看做是传统IO编程模型的两大线程组，
         * bossGroup表示监听端口，accept 新连接的线程组，
         * workerGroup表示处理每一条连接的数据读写的线程组，不理解的同学可以看一下上一小节《Netty是什么》。
         * 用生活中的例子来讲就是，一个工厂要运作，必然要有一个老板负责从外面接活，然后有很多员工，负责具体干活，老板就是bossGroup，员工们就是workerGroup，bossGroup接收完连接，扔给workerGroup去处理。
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        /**
         * 接下来 我们创建了一个引导类 ServerBootstrap，这个类将引导我们进行服务端的启动工作，直接new出来开搞。
         */
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        /**
         * 要启动一个Netty服务端，必须要指定三类属性，分别是线程模型、IO 模型、连接读写处理逻辑，有了这三者，之后在调用bind(8000)，我们就可以在本地绑定一个 8000 端口启动起来
         */
        serverBootstrap
                /**
                 * 我们通过.group(bossGroup, workerGroup)给引导类配置两大线程组，这个引导类的线程模型也就定型了。
                 */
                .group(bossGroup, workerGroup)
                /**
                 * 然后，我们指定我们服务端的 IO 模型为NIO，我们通过.channel(NioServerSocketChannel.class)来指定 IO 模型，当然，这里也有其他的选择，如果你想指定 IO 模型为 BIO，那么这里配置上OioServerSocketChannel.class类型即可，当然通常我们也不会这么做，因为Netty的优势就在于NIO。
                 */
                .channel(NioServerSocketChannel.class)
                /**
                 * 服务端相关的数据处理逻辑是通过 ServerBootstrap 的 childHandler() 方法指定
                 *
                 * 接着，我们调用childHandler()方法，给这个引导类创建一个ChannelInitializer，这里主要就是定义后续每条连接的数据读写，业务处理逻辑，不理解没关系，在后面我们会详细分析。
                 *
                 * 看不懂, 可以不要理解先
                 * NioSocketChannel:对 NIO 类型的连接的抽象 Socket
                 * NioServerSocketChannel:对 NIO 类型的连接的抽象 ServerSocket
                 *
                 * ChannelInitializer这个类中，我们注意到有一个泛型参数NioSocketChannel，这个类呢，就是 Netty 对 NIO 类型的连接的抽象，而我们前面NioServerSocketChannel也是对 NIO 类型的连接的抽象，NioServerSocketChannel和NioSocketChannel的概念可以和 BIO 编程模型中的ServerSocket以及Socket两个概念对应上
                 */
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 在 initChannel() 方法里面给客户端添加一个逻辑处理器，这个处理器的作用就是负责向服务端写数据

                        /**
                         * ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式，这里不理解没关系，后面会讲到
                         *
                         * 然后再调用 addLast() 方法 添加一个逻辑处理器，这个逻辑处理器为的就是在客户端建立连接成功之后，向服务端写数据
                         */
                        // ch.pipeline().addLast(new FirstServerHandler());

                        /**
                         * 使用 ClientHandler 来走登录例子
                         */
                        // ch.pipeline().addLast(new ServerHandler());

                        /**
                         * pipeline 与 channelHandler
                         */
                        // // inBound，处理读数据的逻辑链
                        // ch.pipeline().addLast(new InBoundHandlerA());
                        // ch.pipeline().addLast(new InBoundHandlerB());
                        // ch.pipeline().addLast(new InBoundHandlerC());
                        // // outBound，处理写数据的逻辑链
                        // ch.pipeline().addLast(new OutBoundHandlerA());
                        // ch.pipeline().addLast(new OutBoundHandlerB());
                        // ch.pipeline().addLast(new OutBoundHandlerC());

                        /**
                         * ByteToMessageDecoder
                         * SimpleChannelInboundHandler
                         * MessageToByteEncoder
                         */
                        // 基于长度域拆包器
                        // ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        // ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());

                        /**
                         * StringDecoder
                         */
                        // ch.pipeline().addLast(new StringDecoder());
                        // ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                        // 	@Override
                        // 	protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                        // 		System.out.println(msg);
                        // 	}
                        // });

                    }
                });

        /**
         * handler() 方法
         *
         * 可以和我们前面分析的childHandler()方法对应起来，childHandler()用于指定处理新连接数据的读写处理逻辑，handler()用于指定在服务端启动过程中的一些逻辑，通常情况下呢，我们用不着这个方法。
         */
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            protected void initChannel(NioServerSocketChannel ch) {
                System.out.println("服务端启动中");
            }
        });

        /**
         * attr() 方法
         *
         * attr()方法可以给服务端的 channel，也就是NioServerSocketChannel指定一些自定义属性，然后我们可以通过channel.attr()取出这个属性，比如，上面的代码我们指定我们服务端channel的一个serverName属性，属性值为nettyServer，其实说白了就是给NioServerSocketChannel维护一个map而已，通常情况下，我们也用不上这个方法。
         */
        serverBootstrap.attr(AttributeKey.newInstance("serverName"), "nettyServer");

        /**
         * childAttr() 方法
         *
         * 除了可以给服务端 channel NioServerSocketChannel指定一些自定义属性之外，我们还可以给每一条连接指定自定义属性
         *
         * childAttr可以给每一条连接指定自定义属性，然后后续我们可以通过channel.attr()取出该属性。
         */
        serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"), "clientValue");

        /**
         * childOption() 方法
         *
         * childOption()可以给每条连接设置一些TCP底层相关的属性，比如上面，我们设置了两种TCP属性，其中
         *
         * 1. ChannelOption.SO_KEEPALIVE表示是否开启TCP底层心跳机制，true为开启
         * 2. ChannelOption.TCP_NODELAY表示是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
         */
        serverBootstrap
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);

        /**
         * 除了给每个连接设置这一系列属性之外，我们还可以给服务端channel设置一些属性，最常见的就是so_backlog，如下设置
         *
         * 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
         */
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);

        // /**
        //  * serverBootstrap.bind(8000);这个方法呢，它是一个异步的方法，调用之后是立即返回的，他的返回值是一个ChannelFuture，我们可以给这个ChannelFuture添加一个监听器GenericFutureListener，然后我们在GenericFutureListener的operationComplete方法里面，我们可以监听端口是否绑定成功
        //  */
        // serverBootstrap.bind(8000);

        // /**
        //  * 在上面代码中我们绑定了 8000 端口，接下来我们实现一个稍微复杂一点的逻辑，我们指定一个起始端口号，比如 1000，然后呢，我们从1000号端口往上找一个端口，直到这个端口能够绑定成功，比如 1000 端口不可用，我们就尝试绑定 1001，然后 1002，依次类推。
        //  *
        //  * serverBootstrap.bind(8000);这个方法呢，它是一个异步的方法，调用之后是立即返回的，他的返回值是一个ChannelFuture，我们可以给这个ChannelFuture添加一个监听器GenericFutureListener，然后我们在GenericFutureListener的operationComplete方法里面，我们可以监听端口是否绑定成功
        //  */
        // serverBootstrap.bind(8000).addListener(new GenericFutureListener<Future<? super Void>>() {
        // 	public void operationComplete(Future<? super Void> future) {
        // 		if (future.isSuccess()) {
        // 			System.out.println("端口绑定成功!");
        // 		} else {
        // 			System.err.println("端口绑定失败!");
        // 		}
        // 	}
        // });

        bind(serverBootstrap, PORT);
    }

    /**
     * 我们接下来从 1000 端口号，开始往上找端口号，直到端口绑定成功，我们要做的就是在 if (future.isSuccess())的else逻辑里面重新绑定一个递增的端口号，接下来，我们把这段绑定逻辑抽取出一个bind方法
     *
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }

    private static void bind2(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
