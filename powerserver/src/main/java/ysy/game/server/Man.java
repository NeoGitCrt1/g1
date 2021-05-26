package ysy.game.server;

import ysy.game.model.BodyMeta;
import ysy.game.model.Constant;
import ysy.game.model.GSEvent;

public class Man extends Body {
    public Man(GSEvent meta) {
        super(meta);
    }

    @Override
    public boolean nextFrame(final Body[][] map) {
        char d = (char) meta.msg[0];
        if (d == BodyMeta.Direction.HALT.directCode) {
            return false;
        }
        if (d == BodyMeta.Direction.UP.directCode) {
            y -= 1;
        } else if (d == BodyMeta.Direction.DOWN.directCode) {
            y += 1;
        } else if (d == BodyMeta.Direction.LEFT.directCode) {
            x -= 1;
        } else if (d == BodyMeta.Direction.RIGHT.directCode) {
            x += 1;
        }
        wrap(x, y);
        Body body = map[x][y];
        if (body != null && body instanceof Food) {
            ((Food) body).regen(meta.id);
        }



        return true;
    }
}
