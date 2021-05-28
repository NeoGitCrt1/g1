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
        if (x == myMan.x && y == myMan.y) {
            myMan.isPossessed = true;
            return true;
        }
        int newX = x;
        int newY = y;

        int stepX = myMan.x > newX ? 1 : myMan.x == newX ? 0 : -1;
        int stepY = myMan.y > newY ? 1 : myMan.y == newY ? 0 : -1;

        int startX = stepX == 0 ? (newX - 2) : newX;
        int endX = stepX == 0 ? (newX + 2) : (startX + stepX * 3);
        int startY = stepY == 0 ? (newY - 2) : newY;
        int endY = stepY == 0 ? (newY + 2) : (startY + stepY * 3);
        boolean escape = false;


        for (int i = Math.max(Math.min(startX, endX), 0); i <= Math.min(Math.max(startX, endX), Constant.COLUMNS - 1); i++) {
            for (int j = Math.max(Math.min(startY, endY), 0); j <= Math.min(Math.max(startY, endY), Constant.ROWS - 1); j++) {
                Body m = map[i][j];
                if (m != null && !(m instanceof Food) && m != myMan) {
                    startX = i;
                    startY = j;
                    escape = true;
                    break;
                }
            }
        }
        if (escape) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            stepX = startX > newX ? 1 : startX == newX ? 0 : -1;
            stepY = startY > newY ? 1 : startY == newY ? 0 : -1;

            if (stepY == 0) {
                stepY = random.nextInt(3) - 1;
            }
            if (stepX == 0) {
                stepX = random.nextInt(3) - 1;
            }
            newY += -stepY * random.nextInt(5);
            newX += -stepX * random.nextInt(5);

            wrap(newX, newY);
            return true;

        }

        newX += stepX;
        newY += stepY;

        wrap(newX, newY);
        return true;
    }

    @Override
    protected void wrap(int newX, int newY) {
        if (newX == myMan.x && newY == myMan.y) {
            myMan.isPossessed = true;
        }
        super.wrap(newX, newY);
    }
}
