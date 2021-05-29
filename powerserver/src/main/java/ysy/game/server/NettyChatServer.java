package ysy.game.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import static ysy.game.model.Constant.FIX_LEN;

public class NettyChatServer {


    public static int port = 8888;

    public static void start() {
        // 定义线程组
        EventLoopGroup ioG = new NioEventLoopGroup(1);
        EventLoopGroup wkG = new NioEventLoopGroup(4);

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(ioG, wkG)
                .channel(NioServerSocketChannel.class)
                // 配置tcp参数
                .option(ChannelOption.AUTO_READ, true)
                // 定义pipeline
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 编解码
//                                .addLast(new StringEncoder())
                                .addLast(new FixedLengthFrameDecoder(FIX_LEN))
                                // 处理事件
                                .addLast(new GameEventHandler());
                    }
                })

        ;
        try {
            // 绑定端口启动监听
            ChannelFuture cf = serverBootstrap.bind(port).sync();
            // 监听关闭事件
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            //
        } finally {
            ioG.shutdownGracefully();
            wkG.shutdownGracefully();
        }

    }
}
