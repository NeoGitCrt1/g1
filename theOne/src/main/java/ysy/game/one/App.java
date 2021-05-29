package ysy.game.one;

import ysy.game.model.Constant;
import ysy.game.server.GameServerMain;

public class App {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        Constant.parseArgs(args);
        if (Constant.hasArg(Constant.CLIENT_ON)) {
            ysy.game.client.v1.App.main(args);
        }
        if (Constant.hasArg(Constant.SERVER_ON)) {
            GameServerMain.main(args);
        }
    }
}
