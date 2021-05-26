package ysy.game.server;

import ysy.game.model.Constant;
import ysy.game.model.FoodMeta;
import ysy.game.model.GSEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Food extends Body {
    public Food(FoodMeta meta) {
        super(new GSEvent("padding8", meta));
    }

    public void reset(Body[][] map) {
        map[x][y] = this;
        meta.id = "padding8";
    }

    public void regen(final String id) {
        meta.id = id;
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        x = rand.nextInt(Constant.COLUMNS - 4) + 2;
        y = rand.nextInt(Constant.ROWS - 4) + 2;
        wrap(x, y);
    }

    @Override
    public boolean nextFrame(Body[][] map) {
        return true;
    }
}
