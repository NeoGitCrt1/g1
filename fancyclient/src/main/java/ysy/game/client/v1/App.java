package ysy.game.client.v1;

import ysy.game.model.Constant;

import java.net.InetSocketAddress;

/**
 * Hello world!
 */
public class App {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(App.class);


    public static void main(String[] args) {
        Constant.parseArgs(args);
        NettyChatClient.host = Constant.getArg(Constant.HOST, "localhost");

        try {
            NettyChatClient.port = Constant.getIntArg(Constant.PORT, "8888");
            new InetSocketAddress(NettyChatClient.host, NettyChatClient.port);
        } catch (Exception e) {
            log.error("args error", e);
            return;
        }

        UIMain.start();
        NettyChatClient.start();
        ClientEventHandle.start();
    }
}
