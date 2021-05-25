package ysy.game.client.v1;

/**
 * Hello world!
 */
public class App {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(App.class);


    public static void main(String[] args) {
        log.info("Hello World!");
        UIMain.start();
        NettyChatClient.start();
    }
}
