package ysy.game.server;

import ysy.game.model.Constant;
import ysy.game.model.GSEvent;

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

        int startX = Math.min(oldX, myMan.x);
        int endX = Math.max(oldX, myMan.x);
        int stepX = myMan.x - oldX > 0 ? 1: -1;
        int startY = Math.min(oldY, myMan.y);
        int endY = Math.max(oldY, myMan.y);
        int stepY = myMan.y - oldY > 0 ? 1: -1;
        boolean escape = false;
        for (int i = startX; i <= endX * stepX; i += stepX) {
            for (int j = startY; j <= endY * stepY; j+= stepY) {
                Body m = map[i][j];
                if (m !=null && !(m instanceof Food)) {
                    if (Math.abs(i - oldX) <= 3 && Math.abs(j - oldY) <= 3) {
                        startX = i;
                        startY = j;
                    escape = true;
                    break;
                    }
                }
            }
        }
        if (escape) {
            int absX = Math.abs(startX - oldX);
            int absY = Math.abs(startY - oldY);
            if (absX > absY) {
                oldY += stepY * -1;
            } else if (absX < absY) {
                oldX += stepX * -1;
            } else {
                oldY += stepY * -1;
                oldX += stepX * -1;
            }
            wrap(oldX, oldY);
            return true;

        }


        if (oldX > myMan.x) {
            oldX--;
        } else if (oldX < myMan.x) {
            oldX++;
        }

        if (oldY > myMan.y) {
            oldY--;
        } else if (oldY < myMan.y) {
            oldY++;
        }




//        if (map[oldX][oldY] == myMan) {
//            // TODO tranform myMan
//        }


        wrap(oldX, oldY);
        return true;
    }
}
