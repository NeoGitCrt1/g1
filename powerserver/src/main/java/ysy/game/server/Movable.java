package ysy.game.server;

public interface Movable {
    /**
     * @param map
     * @return true to broadcast new position
     */
    boolean nextFrame(Body[][] map);
}
