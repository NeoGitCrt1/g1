package ysy.game.client.main;

import ysy.game.model.FoodMeta;

import static ysy.game.model.Constant.CELL_SIZE;

// Food is a food item that the snake can eat. It is placed randomly in the pit.
public class Food extends FoodMeta {
    protected Color color = Color.BLUE;

    //Draw itself
    public void draw(Graphics g) {
        g.setColor(color);
        g.fill3DRect(x * CELL_SIZE,
                y * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE,
                true);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
