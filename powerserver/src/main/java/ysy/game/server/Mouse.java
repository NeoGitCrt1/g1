package ysy.game.server;

import ysy.game.model.Constant;
import ysy.game.model.GSEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends Body {
    private final Man myMan;

    public Mouse(GSEvent meta, Man man) {
        super(meta);
        this.myMan = man;
    }

    @Override
    public boolean nextFrame(final Body[][] map) {

        int oldX = x;
        int oldY = y;

        if (oldX == myMan.x && oldY == myMan.y) {
            return false;
        }

        int stepX = myMan.x > oldX ? 1: myMan.x == oldX ? 0 : -1;
        int stepY = myMan.y > oldY ? 1: myMan.y == oldY ? 0 : -1;

        int startX = stepX == 0 ? (oldX - 2): oldX;
        int endX = stepX == 0? (oldX + 2): (startX + stepX * 3);
        int startY = stepY == 0 ? (oldY - 2): oldY;
        int endY = stepY == 0? (oldY + 2): (startY + stepY * 3);
        boolean escape = false;


        for (int i = Math.max(Math.min(startX, endX), 0) ; i <= Math.min(Math.max(startX, endX), Constant.COLUMNS-1) ; i ++) {
            for (int j = Math.max(Math.min(startY, endY), 0) ; j <= Math.min(Math.max(startY, endY), Constant.ROWS-1); j++) {
                Body m = map[i][j];
                if (m !=null && !(m instanceof Food) && m != myMan) {
                    startX = i;
                    startY = j;
                    escape = true;
                    break;
                }
            }
        }
        if (escape) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            if (stepY == 0) {
                stepY = random.nextInt(2) - 1;
            }
            if (stepX == 0) {
                stepX = random.nextInt(2) - 1;
            }
            oldY += -stepY * random.nextInt(5);
            oldX += -stepX * random.nextInt(5);

            wrap(oldX, oldY);
            return true;

        }

        oldX +=stepX;
        oldY +=stepY;
//        if (map[oldX][oldY] == myMan) {
//            // TODO tranform myMan
//        }


        wrap(oldX, oldY);
        return true;
    }
}
