package ysy.game.model;

import java.util.concurrent.ThreadLocalRandom;

public class FoodMeta {
    //current food location(x,y) in cells
    public int x, y;
    //color for display

    //default constructor
    public FoodMeta() {
        //place outside the pit, so that it will not be "displayed"
        x = -1;
        y = -1;
    }

    //Regenerate a food item. Randomly place inside the pit
    public FoodMeta regenerate() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        x = rand.nextInt(Constant.COLUMNS - 4) + 2;
        y = rand.nextInt(Constant.ROWS - 4) + 2;
        return this;
    }

}
