public class Exit {
    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    
    private int row;
    private int col;
    private String direction;

    public Exit(int row, int col, String direction) {
        this.row = row;
        this.col = col;
        this.direction = direction;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getDirection() {
        return direction;
    }

    public int[] getPoint(){
        switch (direction) {
            case UP:
                return new int[]{row - 1, col};
            case DOWN:
                return new int[]{row + 1, col};
            case LEFT:
                return new int[]{row, col - 1};
            case RIGHT:
                return new int[]{row, col + 1};
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }
}
