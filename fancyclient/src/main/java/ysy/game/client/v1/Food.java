package ysy.game.client.v1;

import ysy.game.model.GCEvent;

import javax.swing.*;
import java.awt.*;

public class Food extends Body {
    public final Color c = Color.ORANGE;
    private int score = 0;
    public volatile boolean award = false;

    @Override
    public Body update(GCEvent gcEvent) {
        return super.update(gcEvent);
    }

    public void reset() {
        this.score = 0;
        this.award = false;
    }

    public void draw(Graphics g, JLabel lblScore) {
        g.setColor(c);
        super.draw(g);
        if (award) {
            score++;
            lblScore.setText("Score: " + score);
            g.drawString("+1", wrapped.getInt(1), wrapped.getInt(5));
            award = false;
        }

    }
}
