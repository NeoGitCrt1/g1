package ysy.game.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class GSEvent extends GEvent {
    public String id;

    public GSEvent(String id, FoodMeta f) {
        super(f);
        this.id = id;
    }

    public GSEvent(String id, BodyMeta bodyMeta) {
        super(bodyMeta);
        this.id = id;
    }

    public GSEvent(String id, MouseMeta bodyMeta) {
        super(bodyMeta);
        this.id = id;
    }

    public GSEvent(String id, boolean onOff) {
        super(onOff);
        this.id = id;
    }


    public ByteBuf toBytBuf(final String argid) {
        byte[] idBytes = argid.getBytes();
        ByteBuf message = Unpooled.buffer(idBytes.length + msg.length);
        message.writeBytes(idBytes);
        message.writeBytes(msg);
        return message;
    }

    public ByteBuf toBytBuf() {
        return toBytBuf(this.id);
    }
}
