package ysy.game.client.v1;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(App.class);


    public static void main(String[] args) {
        if (args != null) {
            try {
                Map<String, String> param = new HashMap<>();
                for (int i = 1; i < args.length; i++) {
                    param.put(args[i - 1], args[i]);
                }
                NettyChatClient.host = param.getOrDefault("-h", "localhost");
                NettyChatClient.port = Integer.valueOf(param.getOrDefault("-p", "8888"));
                new InetSocketAddress(NettyChatClient.host, NettyChatClient.port);
            } catch (Exception e) {
                return;
            }
        }

        UIMain.start();
        NettyChatClient.start();
        ClientEventHandle.start();
    }
}
