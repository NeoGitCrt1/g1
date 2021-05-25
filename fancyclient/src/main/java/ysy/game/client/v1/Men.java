package ysy.game.client.v1;

import ysy.game.model.BodyMeta;
import ysy.game.model.GCEvent;

import java.awt.*;

import static ysy.game.model.Constant.CELL_SIZE;

public class Men extends Body{
    public Color c = Color.RED;
    private static final int EYE_SIZE = 3;

    public Men(GCEvent gcEvent) {
        super(gcEvent);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(c);
        BodyMeta.Direction direction = BodyMeta.Direction.ofDirection((char) gcEvent.msg[0]);
        int x = wrapped.getInt(1) * CELL_SIZE ;
        int y = wrapped.getInt(5) * CELL_SIZE;
        g.fill3DRect(x,
                y,
                CELL_SIZE,
                CELL_SIZE,
                true);
        g.setColor(Color.WHITE);
        g.drawRect(direction.calculateX(x, CELL_SIZE), direction.calculateY(y, CELL_SIZE), EYE_SIZE, EYE_SIZE);
    }
}
