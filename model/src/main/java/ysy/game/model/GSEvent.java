package ysy.game.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;


public class GSEvent extends GEvent {
    public final String id;
    public Channel c;

    public GSEvent(String id, FoodMeta f) {
        super(f);
        this.id = id;
    }

    public GSEvent(String id, BodyMeta bodyMeta) {
        super(bodyMeta);
        this.id = id;
    }

    public GSEvent(String id, boolean onOff) {
        super(onOff);
        this.id = id;
    }

    public GSEvent setChannel(Channel c) {
        this.c = c;
        return this;
    }

    public ByteBuf toBytes() {
        byte[] idBytes = id.getBytes();
        ByteBuf message = Unpooled.buffer(idBytes.length + msg.length);
        message.writeBytes(idBytes);
        message.writeBytes(msg);
        return message;
    }
}
