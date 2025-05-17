package app.components;

import java.util.ArrayList;
// import java.util.Arrays;
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

    public Board(Board other){
        this.rows = other.rows;
        this.cols = other.cols;
        this.cell = new Cell[rows][cols];
        this.pieces = new ArrayList<>();

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                this.cell[r][c] = new Cell(other.cell[r][c]);
            }
        }

        for(Piece piece : other.pieces){
            Piece newPiece;
            if(piece instanceof PrimaryPiece){
                newPiece = new PrimaryPiece(piece.getRow(), piece.getCol(), piece.getSymbol(), piece.getLength(), piece.isHorizontal());
                this.addPrimaryPiece((PrimaryPiece) newPiece);
            } else {
                newPiece = new Piece(piece.getRow(), piece.getCol(), piece.getSymbol(), piece.getLength(), piece.isHorizontal());
                this.addPiece(newPiece);
            }

            for(int[] position : newPiece.getFullPosition()){
                this.cell[position[0]][position[1]].occupy(newPiece.getSymbol());
            }
        }

        Exit newExit = other.getExit();
        this.setExit(new Exit(newExit.getRow(), newExit.getCol()));
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
        return exit.isHit();
    }

    public void displayBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = getCell(r, c);
                System.out.print(cell != null ? cell.getSymbol() : '.');
            }
            System.out.println();
        }

        System.out.println("Exit: " + exit.getRow() + ", " + exit.getCol());
    }

    public boolean equalTo(Board other){
        if(this.rows != other.getRows() || this.cols != other.getCols()){
            return false;
        } // jujur bakal sama sih, jadi ngga pake ya yauda

        for(int r = 0; r < this.rows; r++){
            for(int c = 0; c < this.cols; c++){
                char a = this.getCell(r, c).getSymbol();
                char b = other.getCell(r, c).getSymbol();
                if(a != b){
                    return false;
                }
            }
        }

        return true;
    }


    public int calculateCost() {
        int returnValue = 0;
        Piece tempPrimaryPiece = this.getPrimaryPiece();
        Exit tempExit = this.getExit();
        boolean tempIsHorizontal = tempPrimaryPiece.isHorizontal();
        if (tempIsHorizontal) {
            if (tempExit.getCol() == -1) {
                // Cek ke kiri.
                for (int i = tempPrimaryPiece.getCol()-1; i >= 0 ; i--) {
                    if (!this.getCell(tempPrimaryPiece.getRow(), i).isEmpty()) {
                        returnValue ++;
                    }
                }
            } else {
                // Cek ke kanan.
                for (int i = tempPrimaryPiece.getCol() + tempPrimaryPiece.getLength() - 1; i < this.getCols() - 1; i++) {
                    if (!this.getCell(tempPrimaryPiece.getRow(), i).isEmpty()) {
                        returnValue ++;
                    }
                }
            }
        } else {
            if (tempExit.getRow() == -1) {
                // Cek ke atas.
                for (int i = tempPrimaryPiece.getRow()-1; i >= 0; i--) {
                    if (!this.getCell(i, tempPrimaryPiece.getCol()).isEmpty()) {
                        returnValue ++;
                    }
                }
            } else {
                // Cek ke bawah.
                for (int i = tempPrimaryPiece.getRow() + tempPrimaryPiece.getLength() - 1; i < this.getRows() - 1; i++) {
                    if (!this.getCell(i, tempPrimaryPiece.getCol()).isEmpty()) {
                        returnValue ++;
                    }
                }
            }
        }
        return returnValue;
    }

    public int calculateDistanceCost() {
        if (this.getPrimaryPiece().isHorizontal()) {
            return Math.abs(this.getPrimaryPiece().getCol() - this.getExit().getCol());
        }
        return Math.abs(this.getPrimaryPiece().getRow() - this.getExit().getRow());
    }
}