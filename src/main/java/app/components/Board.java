import java.util.ArrayList;
import java.util.List;

public class Board {
    private int rows, cols;
    private Cell[][] cell;
    private List<Piece> pieces;
    private PrimaryPiece primaryPiece;
    private Exit exit;

    public Board(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.cell = new Cell[rows][cols];
        this.pieces = new ArrayList<>();
    }

    public void setCell(int row, int col, Cell cell){
        this.cell[row][col] = cell;
    }

    public Cell getCell(int row, int col){
        return this.cell[row][col];
    }

    public int getRows(){
        return this.rows;
    }

    public int getCols(){
        return this.cols;
    }

    public void addPiece(Piece piece){
        this.pieces.add(piece);
    }

    public void addPrimaryPiece(PrimaryPiece piece){
        this.primaryPiece = piece;
        this.pieces.add(piece);
    }

    public List<Piece> getPieces(){
        return this.pieces;
    }

    public PrimaryPiece getPrimaryPiece(){
        return this.primaryPiece;
    }

    public void setExit(Exit exit){
        this.exit = exit;
    }

    public Exit getExit(){
        return this.exit;
    }

    public boolean isInsideBoard(int row, int col){
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isCellEmpty(int row, int col){
        return isInsideBoard(row, col) && cell[row][col] != null && cell[row][col].isEmpty();
    }

    public boolean isSolved(){
        int[][] filledPositions = primaryPiece.getFullPosition();
        int[] exitPosition = exit.getPoint();
        for (int[] position : filledPositions) {
            if (position[0] == exitPosition[0] && position[1] == exitPosition[1]) {
                return true;
            }
        }
        return false;
    }
}