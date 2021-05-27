package ysy.game.client.v1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.util.concurrent.TimeUnit;

public class NettyChatClient {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NettyChatClient.class);
    private static final int FIX_LEN = 17;
    private static final Bootstrap bs = new Bootstrap();
    public static String host = "localhost";
    public static volatile ChannelFuture cf;

    public static void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        bs.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)

                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // marshalling 序列化对象的解码
//                  socketChannel.pipeline().addLast(MarshallingCodefactory.buildDecoder());
                        // marshalling 序列化对象的编码
//                  socketChannel.pipeline().addLast(MarshallingCodefactory.buildEncoder());

                        // 处理来自服务端的响应信息

                        socketChannel.pipeline()
                                .addLast((new FixedLengthFrameDecoder(FIX_LEN)))
                                .addLast(ClientEventHandle.INS)

                        ;
                    }
                })
        ;

        // 客户端开启
        reconnect();
    }

    static synchronized void reconnect() {
        log.info("::Reconnecting ...");

        if (cf != null) {
            cf.channel().close();
            cf.channel().deregister();
        }

        ChannelFuture channelFuture;
        do {
            try {

                log.info("Reconnecting ........");
                channelFuture = bs.connect(host, 8888);

                log.info("Reconnecting .............x");
                channelFuture.await(5, TimeUnit.SECONDS);


                log.info("Reconnecting .................OPEN:{}>>error:{}", channelFuture.channel().isOpen(), channelFuture.cause());
                if (channelFuture.cause() != null) {
                    channelFuture.channel().close();
                    channelFuture.channel().deregister();
                    Thread.sleep(1000);

                }
            } catch (InterruptedException e) {
                log.info("???");
                return;
            }
        } while (!channelFuture.channel().isOpen());
        channelFuture.channel().config().setOption(ChannelOption.SO_KEEPALIVE, true);
        cf = channelFuture;
        log.info("RE:{}", cf.channel());
    }
}
