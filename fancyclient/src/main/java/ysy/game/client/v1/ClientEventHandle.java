package ysy.game.client.v1;

import ysy.game.model.GCEvent;
import ysy.game.model.GEvent;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientEventHandle extends Thread {
    public static final BlockingQueue<GCEvent> evtQ = new ArrayBlockingQueue<GCEvent>(10);
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientEventHandle.class);
    public static volatile String id;

    @Override
    public void run() {
        try {

            GCEvent firstEvt = evtQ.take();
            id = new String(firstEvt.id, StandardCharsets.UTF_8);
            UIMain.food.update(firstEvt);
            log.info(id);
            while (true) {
                GCEvent evt = evtQ.take();
                String key = new String(evt.id, StandardCharsets.UTF_8);
                byte msgType = evt.msg[0];
                if (msgType == GEvent.FOOD) {
                    UIMain.food.update(key, evt);
                } else if (msgType == GEvent.MOUSE) {

                    Body body = UIMain.mouses.get(key);
                    if (body == null) {
                        Mouse body1 = new Mouse(evt);
                        UIMain.mouses.put(key, body1);
                    } else {
                        synchronized (body) {
                            body.update(evt.msg);
                        }
                    }
                } else if(msgType == GEvent.OFF) {
                    UIMain.mouses.remove(key);
                    UIMain.players.remove(key);
                } else {
                    Body body = UIMain.players.get(key);
                    if (body == null) {
                        log.info("put:{}", key);

                        Man body1 = new Man(evt);
                        if (id.equals(key)) {
                            body1.c = Color.PINK;
                        } else {
                            body1.c = Color.BLUE;
                        }
                        UIMain.players.put(key, body1);
                    } else {
                        synchronized (body) {
                            body.update(evt.msg);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            log.info("end connect");
        }
    }
}
