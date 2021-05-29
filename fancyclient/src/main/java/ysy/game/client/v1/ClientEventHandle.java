package ysy.game.client.v1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import ysy.game.model.BodyMeta;
import ysy.game.model.GCEvent;
import ysy.game.model.GEvent;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ClientEventHandle extends ChannelInboundHandlerAdapter implements Runnable {
    public static final ClientEventHandle INS = new ClientEventHandle();
    private static final Thread TH = new Thread(INS);
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientEventHandle.class);
    private static volatile long id;
    private final BlockingQueue<GCEvent> evtQ = new ArrayBlockingQueue<GCEvent>(10);
    private volatile boolean isForceClose = false;

    public static void start() {
        TH.start();
    }

    public static void prepareRestart() {
        INS.isForceClose = true;
        TH.interrupt();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("active confirmed");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (isForceClose) {
            return;
        }
        log.info("::Fire Reconnect");
        ctx.channel().eventLoop().schedule(() -> {
            UIMain.restart();
        }, 1, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        byte[] idBytes = new byte[8];
        byte[] msgBytes = new byte[9];
        bb.getBytes(0, idBytes, 0, 8).getBytes(8, msgBytes, 0, 9);
        bb.release();
        evtQ.offer(new GCEvent(idBytes, msgBytes));
        // log.info(LocalDateTime.now() + ">>" + ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
    }

    private final ByteBuffer keyByteBuffer = ByteBuffer.allocate(8);

    private long getKey(byte[] idBytes) {
        keyByteBuffer.clear();
        keyByteBuffer.put(idBytes);
        keyByteBuffer.flip();
        return keyByteBuffer.getLong();
    }

    @Override
    public void run() {
        for (; ; ) {
            for (; ; ) {
                try {
                    GCEvent firstEvt = evtQ.take();
                    if (firstEvt.msg[0] == GEvent.WELCOME) {
                        id = getKey(firstEvt.id);
                        firstEvt.msg[0] = GEvent.FOOD;
                        UIMain.food.update(firstEvt);
                        UIMain.UI.renderMsg("Score: 0");
                        log.info("My id: {}", id);
                        isForceClose = false;
                        break;
                    }
                } catch (InterruptedException e) {
//                e.printStackTrace();
                }
            }

            log.debug("isForceClose: {} , evtQ.size: {}", isForceClose, evtQ.size());
            try {
                while (!isForceClose) {
                    GCEvent evt = evtQ.take();
                    long key = getKey(evt.id);
                    byte msgType = evt.msg[0];
                    if (msgType == GEvent.FOOD) {
                        if (key == id) {
                            UIMain.food.award = true;
                        }
                        UIMain.food.update(evt);
                    } else if (msgType == GEvent.MOUSE) {
                        Body body = UIMain.mouses.get(key);
                        if (body == null) {
                            Mouse body1 = new Mouse(evt);
                            UIMain.mouses.put(key, body1);
                        } else {
                            body.update(evt.msg);
                        }
                    } else if (msgType == GEvent.OFF) {
                        UIMain.mouses.remove(key);
                        UIMain.players.remove(key);
                        if (id == key) {
                            UIMain.UI.renderMsg("dead");
                        }
                    } else if (msgType >= BodyMeta.Direction.UP.directCode && msgType <= BodyMeta.Direction.HALT.directCode) {
                        Body body = UIMain.players.get(key);
                        if (body == null) {
                            log.info("put:{}", key);

                            Man body1 = new Man(evt);
                            if (id == key) {
                                UIMain.me = body1;
                                body1.c = Color.PINK;
                            } else {
                                body1.c = Color.decode("0x" + new String(keyByteBuffer.array(), CharsetUtil.UTF_8).substring(2));
                            }
                            UIMain.players.put(key, body1);
                        } else {
                            body.update(evt.msg);
                        }
                    } else {
                        log.debug("Unsuported:{}", (char) msgType);
                    }
                }
            } catch (InterruptedException e) {
                log.info("end connect");
            }
        }
    }
}
