package app.components;

public class PrimaryPiece extends Piece {
    public PrimaryPiece(int row, int col, char symbol, int length, boolean isHorizontal) {
        super(row, col, symbol, length, isHorizontal);
    }

    @Override
    public boolean isValidMove( Board board, boolean forward) {
        int newRow = getRow();
        int newCol = getCol();

        if (isHorizontal()) {
            if(forward){
                newCol ++;
            } else {
                newCol --;
            }
        } else {
            if(forward){
                newRow ++;
            } else {
                newRow --;
            }
        }

        Exit exit = board.getExit();
        int[] exitPosition = exit.getPoint();

        for (int i = 0; i < getLength(); i++) {
            int row = isHorizontal() ? newRow : newRow + i;
            int col = isHorizontal() ? newCol + i : newCol;

            if(!board.isInsideBoard(row, col)) {
                if(row == exitPosition[0] && col == exitPosition[1]) {
                    continue;
                } else{
                    return false;
                }
            }

            Cell cell = board.getCell(row, col);
            if(!cell.isEmpty() && cell.getSymbol() != getSymbol()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Board move(Board board, boolean forward) {
        for(int[] position : getFullPosition()){
            if (board.isInsideBoard(position[0], position[1])) {
                board.getCell(position[0], position[1]).clear();
            }
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

        Exit exit = board.getExit();
        int[] exitPoint = exit.getPoint();
        
        int[][] newPositions = getFullPosition();
        boolean hitExit = false;
        
        for(int[] position : newPositions){
            if (position[0] == exitPoint[0] && position[1] == exitPoint[1]) {
                hitExit = true;
                break;
            }
        }
        

        if (hitExit) {
            exit.setHit(true);
        }

        for(int[] position : newPositions){
            if (board.isInsideBoard(position[0], position[1])) {
                board.getCell(position[0], position[1]).occupy(symbol);
            }
        }

        return board;
    }
}
