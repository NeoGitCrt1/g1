package ysy.game.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import ysy.game.model.Constant;
import ysy.game.model.FoodMeta;
import ysy.game.model.GSEvent;

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
        Constant.parseArgs(args);
        NettyChatServer.port = Constant.getIntArg(Constant.PORT, "8888");
        new GameServerMain().start();
        log.info("GO");
        NettyChatServer.start();
        log.info("END");
    }

    public static void kickOff(String cid) {
        GSEvent gsEvent = new GSEvent(cid, false);
        var deadman = allInfo.remove(gsEvent.id);
        mouseInfo.remove(gsEvent.id);
        map[deadman.x][deadman.y] = null;
        channels.forEach(c -> {
            c.writeAndFlush(gsEvent.toBytBuf());
        });
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
        }
    }


    private void moveMen(final List<Body> sendList) {
        allInfo.forEach((k, g) -> {
            int oldx = g.x;
            int oldy = g.y;
            if (g.nextFrame(map)) {
                map[oldx][oldy] = null;
                map[g.x][g.y] = g;
                sendList.add(g);
            }
        });
    }

    private void moveMouse(final List<Body> sendList) {

        mouseInfo.forEach((k, g) -> {
            if (g.nextFrame(map)) {
                sendList.add(g);
            }
        });
    }


}
