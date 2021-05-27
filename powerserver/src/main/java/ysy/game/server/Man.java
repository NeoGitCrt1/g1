package ysy.game.server;

import ysy.game.model.BodyMeta;
import ysy.game.model.Constant;
import ysy.game.model.GSEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Man extends Body {
    public boolean isPossessed = false;
    private int hp = ThreadLocalRandom.current().nextInt(80, 150);
    public Man(GSEvent meta) {
        super(meta);
    }

    @Override
    public boolean nextFrame(final Body[][] map) {
        if (hp <= 0) {
            return false;
        } else if (hp > 100) {
            hp = 100;
        }

        if (isPossessed) {
            hp--;
            if (hp == 0) {
                GameServerMain.kickOff(meta.id);
                return false;
            }

        } else {
            hp++;
        }

        char d = (char) meta.msg[0];
        if (d == BodyMeta.Direction.HALT.directCode) {
            return false;
        }

        int newX = x;
        int newY = y;

        if (d == BodyMeta.Direction.UP.directCode) {
            newY -= 1;
        } else if (d == BodyMeta.Direction.DOWN.directCode) {
            newY += 1;
        } else if (d == BodyMeta.Direction.LEFT.directCode) {
            newX -= 1;
        } else if (d == BodyMeta.Direction.RIGHT.directCode) {
            newX += 1;
        }
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


        Body body = map[newX][newY];
        if (body != null) {
            if (body instanceof Man || isPossessed) {
                return false;
            }
            if (body instanceof Food) {
                ((Food) body).regen(meta.id);
            }

        }

        isPossessed = false;
        wrap(newX, newY);
        return true;
    }
}
