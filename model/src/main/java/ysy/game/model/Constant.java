package ysy.game.model;

import java.util.HashMap;
import java.util.Map;

public final class Constant {
    // server <-> client message length
    public static final int FIX_LEN = 17;
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
    private static final Map<String, String> arg = new HashMap<>();

    private Constant() {
    }

    public static void parseArgs(String[] args) {
        if (args != null) {
            for (int i = 1; i < args.length; i++) {
                arg.put(args[i - 1], args[i]);
            }
        }
    }

    public static String getArg(String parName, String defaultValue) {
        return arg.getOrDefault(parName, defaultValue);
    }

    public static int getIntArg(String parName, String defaultValue) {
        return Integer.valueOf(arg.getOrDefault(parName, defaultValue));
    }
}
