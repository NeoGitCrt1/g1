package ysy.game.client.v1;

import java.awt.*;

public class Food extends Body {
    public final Color c = Color.ORANGE;

    @Override
    public void draw(Graphics g) {
        g.setColor(c);
        super.draw(g);
    }
}
