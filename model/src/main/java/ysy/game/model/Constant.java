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
    private static final Map<String, String> argMap = new HashMap<>();

    private Constant() {
    }

    public static final String CLIENT_ON = "-C";
    public static final String SERVER_ON = "-S";
    public static final String HOST = "-h";
    public static final String PORT = "-c";
    public static void parseArgs(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.toLowerCase().equals(arg)) {
                    try {
                        argMap.put(arg, args[i + 1]);
                    } catch (Exception e) {
                        System.err.println("the value of " + arg + " is not provided!");
                        System.exit(1);
                    }
                    i++;
                } else {
                    argMap.put(arg, null);
                }

            }
        }
    }

    public static String getArg(String parName, String defaultValue) {
        return argMap.getOrDefault(parName, defaultValue);
    }

    public static boolean hasArg(String parName) {
        return argMap.containsKey(parName);
    }

    public static int getIntArg(String parName, String defaultValue) {
        return Integer.valueOf(argMap.getOrDefault(parName, defaultValue));
    }
}
