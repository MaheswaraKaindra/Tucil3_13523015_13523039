package app.components;

public class Cell {
    private int row;
    private int col;
    private char symbol;
    
    public Cell(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
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

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isEmpty(){
        return this.symbol == '.';
    }

    public void occupy(char pieceId){
        this.symbol = pieceId;
    }

    public void clear(){
        this.symbol = '.';
    }
}
