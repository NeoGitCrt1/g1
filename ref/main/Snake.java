package ysy.game.client.main;

import static ysy.game.model.Constant.CELL_SIZE;


/*
 * A snake is made up of one or more SnakeSegment. The first SnakeSegment is the "head"
 * of the snake . The last is the "tail". As the snake moves, it adds one cell to the head
 * and then removes one from the tail
 * if the snake eats food, the head adds one cell but the tail will not shrink
 * */
public class Snake {
    private static final int INIT_LENGTH = 3; //snake's cells
    private final Color color; // = Color.BLACK; // color for the snake body
    public SnakeSegment headSegment;
    //private Color colorHead = Color.GREEN; // color for the head
    private Direction direction; // get the current direction of the snake's head
    private boolean dirUpdatePending; //Pending update for a direction change?
    private Random random = new Random(); // randomly regenerating a snake


    public Snake() {
        this(true);
    }

    public Snake(boolean isMe) {
        color = isMe ?
                Color.GREEN :
                Color.BLACK;
        ;
    }

    //Regenerate the snake
    public void regenerate() {
        //Randomly generate a snake inside a pit
//		int length = INIT_LENGTH; // 3 cells
//		int headX = random.nextInt(GameMain.COLUMNS - length * 2) + length;
//		int headY = random.nextInt(GameMain.ROWS - length * 2) + length;
//		direction = Direction
//				.values()[random.nextInt(Direction.values().length)];
//		headSegment = new SnakeSegment(headX, headY, length, direction);
//		dirUpdatePending = false;

    }

    //Change the direction of the snake, but no 180 degree turn allowed
    public void setDirection(Direction newDir) {
        // Ignore if there is a direction change pending and no 180 degree turn
        if (!dirUpdatePending
                && (newDir != direction)
                && ((newDir == Direction.UP && direction != Direction.DOWN)
                || (newDir == Direction.DOWN && direction != Direction.UP)
                || (newDir == Direction.LEFT && direction != Direction.RIGHT)
                || (newDir == Direction.RIGHT && direction != Direction.LEFT
        ))) {
            int x = headSegment.getHeadX();
            int y = headSegment.getHeadY();
            direction = newDir;
            dirUpdatePending = true; //will be cleared after updated
        }

    }

    //Move the snake by one step. The snake "head" segment grows by one cell
    //the rest of the segments remain unchanged. The "tail" will later be shrink
    //if collision detected
    public void update() {
        headSegment.grow();
        dirUpdatePending = false; //can process the key input again
    }

    //Not eaten a food item. Shrink the tail by one cell
    public void shrink() {
        headSegment.shrink();
    }

    //Get the X,Y coordinate of the cell that contains the snake's head segment
    public int getHeadX() {
        return headSegment.getHeadX();
    }

    public int getHeadY() {
        return headSegment.getHeadY();
    }

    // Returns true if the snake contains the given (x,y) cell, Used in collision dectection
    public boolean contains(int x, int y) {
        return headSegment.contains(x, y);
    }

    // return true if the snake eats itself
    public boolean eatItself() {
        int headX = getHeadX();
        int headY = getHeadY();
        //eat itself if the headX, headY hits its body segment (4th onwards)
        for (int i = 3; i < snakeSegments.size(); ++i) {
            SnakeSegment segment = snakeSegments.get(i);
            if (segment.contains(headX, headY)) return true;
        }
        return false;
    }

    // Draw itself
    public void draw(Graphics g) {
        g.setColor(color);
        g.fill3DRect(
                getHeadX() * CELL_SIZE,
                getHeadY() * CELL_SIZE,
                CELL_SIZE - 1,
                CELL_SIZE - 1,
                true
        );
    }

    // For debugging
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Snake[dir=" + direction + "\n")
                .append(headSegment)
                .append("]");
        return sb.toString();
    }


    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }


}
