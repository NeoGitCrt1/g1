package ysy.game.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ysy.game.model.BodyMeta;
import ysy.game.model.GSEvent;


public class GameEventHandler extends ChannelInboundHandlerAdapter {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GameEventHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String key = channel.id().asShortText();
        channel.writeAndFlush(new GSEvent(key, GameServerMain.food).toBytes());
        GameServerMain.channels.add(channel);
        BodyMeta bodyMeta = new BodyMeta(BodyMeta.Direction.UP);
        GameServerMain.allInfo.put(key, new Body(new GSEvent(key, bodyMeta)));
        log.info("{}>>online::{}", channel.remoteAddress(), bodyMeta);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        GSEvent gsEvent = new GSEvent(channel.id().asShortText(), false);
        GameServerMain.allInfo.remove(gsEvent.id);
        GameServerMain.channels.remove(channel);
        GameServerMain.channels.forEach(c -> {
            c.writeAndFlush(gsEvent.toBytes());
        });
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
    }

}
