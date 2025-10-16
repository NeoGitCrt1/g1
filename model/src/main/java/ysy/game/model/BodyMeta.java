package ysy.game.model;

import java.util.concurrent.ThreadLocalRandom;

public class BodyMeta {

    public final char d;
    public final int x, y;

    public BodyMeta() {
        this(Direction.rand());
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
            public int calculateX(int x, int cellSize, int tarSize) {
                return x + cellSize / 2 - tarSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize, int tarSize) {
                return y - tarSize / 2;
            }


        }, DOWN('B') {
            @Override
            public int calculateX(int x, int cellSize, int tarSize) {
                return x + cellSize / 2 - tarSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize, int tarSize) {
                return y + cellSize - tarSize / 2;
            }
        }, LEFT('C') {
            @Override
            public int calculateX(int x, int cellSize, int tarSize) {
                return x - tarSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize, int tarSize) {
                return y + cellSize / 2 - tarSize / 2;
            }
        }, RIGHT('D') {
            @Override
            public int calculateX(int x, int cellSize, int tarSize) {
                return x + cellSize - tarSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize, int tarSize) {
                return y + cellSize / 2 - tarSize / 2;
            }
        }, HALT('E') {
            @Override
            public int calculateX(int x, int cellSize, int tarSize) {
                return x + cellSize / 2 - tarSize / 2;
            }

            @Override
            public int calculateY(int y, int cellSize, int tarSize) {
                return y + cellSize / 2 - tarSize / 2;
            }
        };

        public final char directCode;

        Direction(char directCode) {
            this.directCode = directCode;
        }

        public static Direction rand() {
            int i = ThreadLocalRandom.current().nextInt(4);
            return Direction.values()[i];
        }

        public static Direction ofDirection(char c) {
            return c == UP.directCode ? UP : c == DOWN.directCode ? DOWN : c == LEFT.directCode ? LEFT : c == RIGHT.directCode ? RIGHT : HALT;
        }

        public abstract int calculateX(int x, int cellSize, int tarSize);

        public abstract int calculateY(int y, int cellSize, int tarSize);

    }
}
