package ysy.game.model;

public class Constant {
    public static final String TITLE = "Java 2D - Snake hunting";
    //number of rows (in cells)
    public static final int ROWS = 40;
    //number of columns (in cells)
    public static final int COLUMNS = 40;
    //Size of a cell (in pixels)
    public static final int CELL_SIZE = 15;
    //width and height of the game screen
    public static final int CANVAS_WIDTH = COLUMNS * CELL_SIZE;
    public static final int CANVAS_HEIGHT = ROWS * CELL_SIZE;
    //number of game update per second = 3;
    public static final int UPDATE_PER_SEC = 3;
    //per nanoseconds
    public static final long UPDATE_PERIOD_NSEC = 1000000000L / UPDATE_PER_SEC;

}
