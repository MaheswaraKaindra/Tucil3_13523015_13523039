public class PrimaryPiece extends Piece {
    public PrimaryPiece(int row, int col, char symbol, int length, boolean isHorizontal) {
        super(row, col, symbol, length, isHorizontal);
    }

    public boolean isAtExit(Exit exit) {
        int[][] positions = getFullPosition();
        for(int[] position : positions) {
            if(isHorizontal() && position[0] == exit.getRow() && position[1] + length - 1 == exit.getCol()) {
                return true;
            } else if(!isHorizontal() && position[0] + length - 1 == exit.getRow() && position[1] == exit.getCol()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isValidMove(int steps, Board board) {
        int newRow = getRow();
        int newCol = getCol();

        if (isHorizontal()) {
            newCol += steps;
        } else {
            newRow += steps;
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
}
