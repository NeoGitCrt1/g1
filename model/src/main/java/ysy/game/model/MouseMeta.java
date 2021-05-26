package ysy.game.model;

import java.util.concurrent.ThreadLocalRandom;

public class MouseMeta {
    public final int x, y;

    public MouseMeta(BodyMeta bodyMeta) {
        int delta;
        int d;
        do {
            delta = ThreadLocalRandom.current().nextInt(Constant.CELL_SIZE) * 2 + Constant.CELL_SIZE;
            if (ThreadLocalRandom.current().nextDouble() > 0.5) {
                delta = -1 * delta;
            }
            d = bodyMeta.x + delta;
        } while (d <= 0 || d >= Constant.ROWS);
        this.x = d;
        do {
            delta = ThreadLocalRandom.current().nextInt(Constant.CELL_SIZE) * 2 + Constant.CELL_SIZE;
            if (ThreadLocalRandom.current().nextDouble() > 0.5) {
                delta = -1 * delta;
            }
            d = bodyMeta.y + delta;
        } while (d <= 0 || d >= Constant.COLUMNS);

        this.y = d;
    }
}
