package ysy.game.client.v1;

import ysy.game.model.GCEvent;

import java.awt.*;

public class Mouse extends Body {
    private final Color c = Color.BLACK;

    public Mouse(GCEvent gcEvent) {
        super(gcEvent);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(c);
        super.draw(g);
    }
}
