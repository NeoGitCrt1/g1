package ysy.game.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import ysy.game.model.FoodMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameServerMain extends Thread {
    static final ChannelGroup channels = new DefaultChannelGroup(
            "containers", GlobalEventExecutor.INSTANCE);
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GameServerMain.class);
    static FoodMeta food = new FoodMeta().regenerate();
    static Map<String, Body> allInfo = new HashMap<>();

    public static void main(String[] args) {
        new GameServerMain().start();
        log.info("GO");
        NettyChatServer.start();
        log.info("END");
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                Thread.sleep(100);
                final Collection<Body> poses = allInfo.values();
                if (poses.isEmpty()) {
                    continue;
                }
                poses.forEach(g -> {
                    g.nextFrame();
                });
                channels.forEach(c -> {
                    for (Body g : poses) {
                        c.writeAndFlush(g.meta.toBytes());
                    }
                });


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
