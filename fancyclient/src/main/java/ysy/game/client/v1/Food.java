package ysy.game.client.v1;

import ysy.game.model.GCEvent;

import javax.swing.*;
import java.awt.*;

public class Food extends Body {
    public final Color c = Color.ORANGE;
    private boolean award = false;

    public int score = 0;

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

    public Body update(String key, GCEvent gcEvent) {
        if (ClientEventHandle.id.equals(key)) {
            award = true;
        }
        return super.update(gcEvent);
    }
}
