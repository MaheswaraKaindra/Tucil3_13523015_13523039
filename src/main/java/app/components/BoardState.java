package app.components;

public class BoardState {
    private Board board;
    private int cost;
    private BoardState parent;

    public BoardState(Board board, int cost, BoardState parent) {
        this.board = board;
        this.cost = cost;
        this.parent = parent;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getCost() {
        return this.cost;
    }

    public BoardState getParent() {
        return this.parent;
    }
}
