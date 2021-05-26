package ysy.game.model;

import java.util.concurrent.ThreadLocalRandom;

public class BodyMeta {

    public final char d;
    public final int x, y;

    public BodyMeta(char d, int x, int y) {
        this.d = d;
        this.x = x;
        this.y = y;
    }

    public BodyMeta(Direction d) {
        this.d = d.directCode;
        int dd;
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        do {
            dd = rand.nextInt(Constant.ROWS - 4) + 2;
        } while (dd <= 0 || dd >= Constant.ROWS);
        x = dd;
        do {
            dd = rand.nextInt(Constant.COLUMNS - 4) + 2;
        } while (dd <= 0 || dd >= Constant.COLUMNS);
        y = dd;
    }

    @Override
    public String toString() {
        return "BodyMeta{" +
                "d=" + d +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public enum Direction {
        UP('A') {
            @Override
            public int calculateX(int x, int cellSize) {
                return x + cellSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize) {
                return y + cellSize / 5;
            }


        }, DOWN('B') {
            @Override
            public int calculateX(int x, int cellSize) {
                return x + cellSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize) {
                return y + (cellSize / 5) * 4;
            }
        }, LEFT('C') {
            @Override
            public int calculateX(int x, int cellSize) {
                return x + cellSize / 5;
            }

            @Override
            public int calculateY(int y, int cellSize) {
                return y + cellSize / 2;
            }
        }, RIGHT('D') {
            @Override
            public int calculateX(int x, int cellSize) {
                return x + (cellSize / 5) * 4;
            }

            @Override
            public int calculateY(int y, int cellSize) {
                return y + cellSize / 2;
            }
        }, HALT('E') {
            @Override
            public int calculateX(int x, int cellSize) {
                return x + cellSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize) {
                return y + cellSize / 2;
            }
        };

        public final char directCode;

        Direction(char directCode) {
            this.directCode = directCode;
        }

        public static Direction ofDirection(char c) {
            return c == UP.directCode ? UP : c == DOWN.directCode ? DOWN : c == LEFT.directCode ? LEFT : c == RIGHT.directCode ? RIGHT : c == HALT.directCode ? HALT : null;
        }

        public abstract int calculateX(int x, int cellSize);

        public abstract int calculateY(int y, int cellSize);

    }
}
