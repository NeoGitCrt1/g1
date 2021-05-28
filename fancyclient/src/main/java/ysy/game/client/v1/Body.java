package ysy.game.client.v1;

import ysy.game.model.GCEvent;

import java.awt.*;
import java.nio.ByteBuffer;

import static ysy.game.model.Constant.CELL_SIZE;

public class Body implements Drawable {


    public GCEvent gcEvent;
    protected ByteBuffer wrapped;

    public Body(GCEvent gcEvent) {
        this.gcEvent = gcEvent;
        wrapped = ByteBuffer.wrap(gcEvent.msg);
    }

    public Body() {
    }

    public Body update(GCEvent gcEvent) {
        this.gcEvent = gcEvent;
        wrapped = ByteBuffer.wrap(gcEvent.msg);
        return this;
    }

    public synchronized Body update(byte[] s) {
        gcEvent.msg = s;
        wrapped.clear();
        wrapped.put(s);
        return this;
    }

    public synchronized void changeDirect(char d) {
        byte[] msg = gcEvent.msg;
        msg[0] = (byte) d;
        update(msg);
    }

    @Override
    public void draw(Graphics g) {

        g.fill3DRect(wrapped.getInt(1) * CELL_SIZE,
                wrapped.getInt(5) * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE,
                true);
    }


    @Override
    public String toString() {
        return "Body>" + (char) (gcEvent.msg[0]) + ",x:" + wrapped.getInt(1) + ",y:" + wrapped.getInt(5);
    }
}
