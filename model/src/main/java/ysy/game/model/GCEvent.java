package ysy.game.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GCEvent extends GEvent {
    public final byte[] id;

    public GCEvent(byte[] id, BodyMeta bodyMeta) {
        super(bodyMeta);
        this.id = id;
    }

    public GCEvent(byte[] id, byte[] array) {
        super.msg = array;
        this.id = id;
    }

    public ByteBuf toByteBuf() {
        ByteBuf message = Unpooled.buffer(id.length + msg.length);
        message.writeBytes(id);
        message.writeBytes(msg);
        return message;
    }
}
