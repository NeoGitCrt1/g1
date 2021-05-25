package ysy.game.server;

import ysy.game.model.BodyMeta;
import ysy.game.model.Constant;
import ysy.game.model.GSEvent;

import java.nio.ByteBuffer;

public class Body {
    public final GSEvent meta;
    private ByteBuffer wrapped;
    private int x, y;

    public Body(GSEvent meta) {
        this.meta = meta;
        wrapped = ByteBuffer.wrap(meta.msg);
        x = wrapped.getInt(1);
        y = wrapped.getInt(5);
    }

    public void nextFrame() {
        char d = (char) meta.msg[0];
        if (d == BodyMeta.Direction.UP.directCode) {
            y -= 1;
            if (y < 0) {
                y = Constant.ROWS;
            }
        } else if (d == BodyMeta.Direction.DOWN.directCode) {
            y += 1;

            if (y > Constant.ROWS) {
                y = 0;
            }
        } else if (d == BodyMeta.Direction.LEFT.directCode) {
            x -= 1;

            if (x < 0) {
                x = Constant.COLUMNS;
            }
        } else if (d == BodyMeta.Direction.RIGHT.directCode) {
            x += 1;

            if (x > Constant.COLUMNS) {
                x = 0;
            }
        }
        wrapped.putInt(1, x);
        wrapped.putInt(5, y);

    }


}
