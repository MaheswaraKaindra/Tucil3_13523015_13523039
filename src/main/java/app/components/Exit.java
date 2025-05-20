package app.components;

public class Exit {
    private int row;
    private int col;
    private boolean isHit;

    public Exit(int row, int col) {
        this.row = row;
        this.col = col;
        this.isHit = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public int[] getPoint(){
        return new int[]{row, col};
    }
}
