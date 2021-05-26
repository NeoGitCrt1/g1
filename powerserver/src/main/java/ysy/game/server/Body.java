package ysy.game.server;

import ysy.game.model.Constant;
import ysy.game.model.GSEvent;

import java.nio.ByteBuffer;

public abstract class Body implements Movable, Broadcastable {
    public final GSEvent meta;
    protected ByteBuffer wrapped;
    protected int x, y;

    public Body(GSEvent meta) {
        this.meta = meta;
        wrapped = ByteBuffer.wrap(meta.msg);
        x = wrapped.getInt(1);
        y = wrapped.getInt(5);
    }

    protected void wrap(int oldX, int oldY) {
        if (oldY < 0) {
            oldY = Constant.ROWS - 1;
        } else if (oldY >= Constant.ROWS) {
            oldY = 0;
        }
        if (oldX < 0) {
            oldX = Constant.COLUMNS - 1;
        } else if (oldX >= Constant.COLUMNS) {
            oldX = 0;
        }
        this.x = oldX;
        this.y = oldY;
        wrapped.putInt(1, x);
        wrapped.putInt(5, y);
    }


    @Override
    public GSEvent getGsevent() {
        return meta;
    }
}
