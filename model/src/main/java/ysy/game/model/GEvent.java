package ysy.game.model;

import java.nio.ByteBuffer;

public class GEvent {
    public static final char FOOD = '0';
    public static final char MOUSE = '!';

    public static final char ON = 'a';
    public static final char OFF = 'b';
    private static final int LEN = 9;

    public byte[] msg;

    protected GEvent() {
    }

    /**
     * food eaten
     *
     * @param f
     */
    public GEvent(FoodMeta f) {
        msg = new byte[LEN];
        msg[0] = FOOD;
        final ByteBuffer intBytes = ByteBuffer.wrap(msg);
        intBytes.putInt(1, f.x);
        intBytes.putInt(5, f.y);
    }

    /**
     * report position state
     *
     * @param bodyMeta
     */
    public GEvent(BodyMeta bodyMeta) {
        msg = new byte[LEN];
        msg[0] = (byte) bodyMeta.d;
        final ByteBuffer intBytes = ByteBuffer.wrap(msg);
        intBytes.putInt(1, bodyMeta.x);
        intBytes.putInt(5, bodyMeta.y);
    }

    public GEvent(MouseMeta mouseMeta) {
        msg = new byte[LEN];
        msg[0] = MOUSE;
        final ByteBuffer intBytes = ByteBuffer.wrap(msg);
        intBytes.putInt(1, mouseMeta.x);
        intBytes.putInt(5, mouseMeta.y);
    }

    /**
     * online
     */
    public GEvent(boolean isOn) {
        msg = new byte[LEN];
        msg[0] = isOn ? (byte) ON : (byte) OFF;
    }
}
