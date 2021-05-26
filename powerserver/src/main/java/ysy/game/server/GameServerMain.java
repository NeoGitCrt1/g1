package ysy.game.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import ysy.game.model.Constant;
import ysy.game.model.FoodMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GameServerMain extends Thread {
    static final ChannelGroup channels = new DefaultChannelGroup(
            "containers", GlobalEventExecutor.INSTANCE);
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GameServerMain.class);
    static Food food = new Food(new FoodMeta().regenerate());
    static ConcurrentMap<String, Body> allInfo = new ConcurrentHashMap<>();
    static ConcurrentMap<String, Body> mouseInfo = new ConcurrentHashMap<>();
    static Body[][] map = new Body[Constant.ROWS][Constant.COLUMNS];

    public static void main(String[] args) {
        new GameServerMain().start();
        log.info("GO");
        NettyChatServer.start();
        log.info("END");
    }

    @Override
    public void run() {
        final List<Body> sendList = new ArrayList<>();
        try {
            for (; ; ) {
                Thread.sleep(100);
                if (allInfo.isEmpty()) {
                    continue;
                }
                sendList.clear();

                food.reset(map);

                moveMen(sendList);

                moveMouse(sendList);

                channels.forEach(c -> {
                    c.writeAndFlush(food.getGsevent().toBytBuf());
                    for (Body g : sendList) {
                        c.writeAndFlush(g.getGsevent().toBytBuf());
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveMouse(final List<Body> sendList) {
        mouseInfo.forEach((k, g) -> {
            map[g.x][g.y] = null;
            if (g.nextFrame(map)) {
                sendList.add(g);
            }
            map[g.x][g.y] = g;
        });
    }

    private void moveMen(final List<Body> sendList) {
        allInfo.forEach((k, g) -> {
            map[g.x][g.y] = null;
            if (g.nextFrame(map)) {
                sendList.add(g);
            }
            map[g.x][g.y] = g;
        });
    }

}
