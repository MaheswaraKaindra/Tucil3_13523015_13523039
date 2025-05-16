package app.components;

public class Piece {
    protected int row;
    protected int col;
    protected char symbol;
    protected int length;
    protected boolean isHorizontal;

    public Piece(int row, int col, char symbol, int length, boolean isHorizontal) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.length = length;
        this.isHorizontal = isHorizontal;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getLength() {
        return length;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setStartPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int[][] getFullPosition(){
        int[][] positions = new int[length][2];
        for (int i = 0; i < length; i++) {
            if (isHorizontal) {
                positions[i][0] = row;
                positions[i][1] = col + i;
            } else {
                positions[i][0] = row + i;
                positions[i][1] = col;
            }
        }
        return positions;
    }

    public boolean isValidMove(Board board, boolean forward) {
        int newRow = row;
        int newCol = col;

        if (isHorizontal) {
            if(forward){
                newCol ++;
            } else{
                newCol --;
            }
        } else {
            if(forward){
                newRow ++;
            } else{
                newRow --;
            }
        }

        if (!board.isInsideBoard(newRow, newCol)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            int checkRow = isHorizontal ? newRow : newRow + i;
            int checkCol = isHorizontal ? newCol + i : newCol;

            if (!board.isInsideBoard(checkRow, checkCol) || (!board.getCell(checkRow, checkCol).isEmpty() && board.getCell(checkRow, checkCol).getSymbol() != symbol)) {
                return false;
            }
        }
        return true;
    }

    public Board move(Board board, boolean forward) {
        for(int[] position : getFullPosition()){
            board.getCell(position[0], position[1]).clear();
        }

        if (isHorizontal) {
            if(forward){
                col++;
            } else {
                col--;
            }
        } else {
            if(forward){
                row++;
            } else {
                row--;
            }
        }

        for(int[] position : getFullPosition()){
            board.getCell(position[0], position[1]).occupy(symbol);
        }
        return board;
    }
}