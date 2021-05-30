package ysy.game.client.v1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.util.concurrent.TimeUnit;

import static ysy.game.model.Constant.FIX_LEN;

public class NettyChatClient {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NettyChatClient.class);
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    public static String host = "localhost";
    public static int port = 8888;
    public static volatile ChannelFuture cf;

    public static void start() {

        Bootstrap bs = new Bootstrap();
        bs.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)

                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 处理来自服务端的响应信息
                        socketChannel.pipeline()
                                .addLast((new FixedLengthFrameDecoder(FIX_LEN)))
                                .addLast(ClientEventHandle.INS)

                        ;
                    }
                })
        ;

        cf = bs.connect(host, port).addListeners(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    log.info("Reconnect");
                    channelFuture.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            start();
                        }
                    }, 1L, TimeUnit.SECONDS);
                } else {
                    log.info("{}", channelFuture.channel());
                }
            }
        });
    }

    static synchronized void reconnect() {
        log.info("::Reconnecting ...");
        if (cf != null) {
            cf.channel().close();
        }
        start();
    }
}
