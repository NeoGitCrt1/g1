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

    protected void wrap(int newX, int newY) {
        if (newY < 0) {
            newY = Constant.ROWS - 1;
        } else if (newY >= Constant.ROWS) {
            newY = 0;
        }
        if (newX < 0) {
            newX = Constant.COLUMNS - 1;
        } else if (newX >= Constant.COLUMNS) {
            newX = 0;
        }
        this.x = newX;
        this.y = newY;
        wrapped.putInt(1, x);
        wrapped.putInt(5, y);
    }


    @Override
    public GSEvent getGsevent() {
        return meta;
    }
}
