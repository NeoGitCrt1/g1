package ysy.game.client.v1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import ysy.game.model.GCEvent;

import java.util.concurrent.TimeUnit;

public class NettyChatClient {
    private static final int FIX_LEN = 17;
    public static ChannelFuture cf;

    private static ClientEventHandle clientEventHandle;

    public static void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);

        final Bootstrap bs = new Bootstrap();

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
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                        clientEventHandle.interrupt();
                                        clientEventHandle = null;
                                        ClientEventHandle.evtQ.clear();
                                        System.out.println(Thread.currentThread().getName() + "::Fire Reconnect");
                                        ctx.channel().eventLoop().schedule(() -> {
                                            reconnect(bs);
                                        }, 1, TimeUnit.SECONDS);
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf bb = (ByteBuf) msg;
                                        byte[] idBytes = new byte[8];
                                        byte[] msgBytes = new byte[9];
                                        bb.getBytes(0, idBytes, 0, 8).getBytes(8, msgBytes, 0, 9);
                                        bb.release();
                                        ClientEventHandle.evtQ.offer(new GCEvent(idBytes, msgBytes));
                                        // System.out.println(LocalDateTime.now() + ">>" + ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
                                    }
                                })

                        ;
                    }
                })
        ;

        // 客户端开启
        reconnect(bs);

    }

    private static void reconnect(Bootstrap bs) {
        System.out.println(Thread.currentThread().getName() + "::Reconnecting ...");
        ChannelFuture channelFuture;
        do {
            try {
                System.out.println("Reconnecting ........");
                channelFuture = bs.connect("localhost", 8888);

                System.out.println("Reconnecting .............x");
                channelFuture.await(1, TimeUnit.SECONDS);


                System.out.println("Reconnecting .................OPEN:" + channelFuture.channel().isOpen() + ">>" + channelFuture.cause());
                if (channelFuture.cause() != null) {
                    channelFuture.channel().close();
                    channelFuture.channel().deregister();
                    Thread.sleep(1000);

                }
            } catch (InterruptedException e) {
                System.out.println("???");
                return;
            }
        } while (!channelFuture.channel().isOpen());
        cf = channelFuture;
        clientEventHandle = new ClientEventHandle();
        clientEventHandle.start();
        System.out.println("RE:" + cf.channel());
    }
}
