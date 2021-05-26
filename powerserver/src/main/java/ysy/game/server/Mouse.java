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

        for (int i = startX; (stepX > 0 && i <= endX) || (stepX < 0 && i >= endX); i += stepX) {
            for (int j = startY; (stepY > 0 && j <= endY) || (stepY < 0 && j >= endY); j+= stepY) {
                Body m = map[i][j];
                if (m !=null && !(m instanceof Food) && m != myMan) {
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
                oldY += stepY * 3;
            } else if (absX < absY) {
                oldX += stepX * 3;
            } else {
                oldY += stepY * 3;
                oldX += stepX * 3;
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
