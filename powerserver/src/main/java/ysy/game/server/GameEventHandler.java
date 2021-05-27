package ysy.game.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ysy.game.model.BodyMeta;
import ysy.game.model.GSEvent;
import ysy.game.model.MouseMeta;


public class GameEventHandler extends ChannelInboundHandlerAdapter {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GameEventHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String key = channel.id().asShortText();
        GSEvent gsevent = new GSEvent(key, true);
        gsevent.welCome();
        channel.writeAndFlush(gsevent.toBytBuf(key));
        GameServerMain.channels.add(channel);
        BodyMeta bodyMeta = new BodyMeta(BodyMeta.Direction.UP);
        MouseMeta mouseMeta = new MouseMeta(bodyMeta);
        Man man = new Man(new GSEvent(key, bodyMeta));
        GameServerMain.allInfo.put(key, man);
        GameServerMain.mouseInfo.put(key, new Mouse(new GSEvent(key, mouseMeta), man));

        log.info("{}>>online::{}", channel.remoteAddress(), bodyMeta);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        GameServerMain.kickOff(channel.id().asShortText());
        GameServerMain.channels.remove(channel);
        log.info("{}<<offline", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object payload) {
        ByteBuf bb = (ByteBuf) payload;
        Body body = GameServerMain.allInfo.get(ctx.channel().id().asShortText());
        byte evtType = bb.getByte(8);
        if (BodyMeta.Direction.ofDirection((char) evtType) != null) {
            body.meta.msg[0] = evtType;
        }
        bb.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.warn("{}<<exception", cause.getMessage());
    }

}
