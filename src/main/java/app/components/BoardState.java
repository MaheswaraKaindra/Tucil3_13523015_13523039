package app.components;

import java.util.ArrayList;
import java.util.List;

public class BoardState {
    private Board board;
    private int cost;
    private int pathCost;
    private int distanceCost;
    private BoardState parent;

    public BoardState(Board board, int cost, int pathCost, BoardState parent){
        this.board = board;
        this.cost = cost;
        this.pathCost = pathCost;
        this.distanceCost = board.calculateDistanceCost();
        this.parent = parent;
    }

    public BoardState(Board board, int pathCost, BoardState parent){
        this.board = board;
        this.cost = board.calculateCost();
        this.pathCost = pathCost;
        this.distanceCost = board.calculateDistanceCost();
        this.parent = parent;
    }

    public BoardState(Board board, BoardState parent){
        this.board = board;
        this.cost = board.calculateCost();
        this.pathCost = 0;
        this.distanceCost = board.calculateDistanceCost();
        this.parent = parent;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getCost() {
        return this.cost;
    }

    public int getPathCost() {
        return this.pathCost;
    }

    public int getDistanceCost() {
        return this.distanceCost;
    }

    public BoardState getParent() {
        return this.parent;
    }

    private Piece findPiece(Board board, char symbol) {
        for (Piece piece : board.getPieces()) {
            if (piece.getSymbol() == symbol) {
                return piece;
            }
        }
        return null;
    }

    public List<BoardState> generatePath(){
        List <BoardState> path = new ArrayList<>();
        Board currentBoard = this.board;

        for(Piece piece : currentBoard.getPieces()){
            for(boolean forward : new boolean[]{true, false}){
                if(piece.isValidMove(currentBoard, forward)){
                    Board newBoard = new Board(currentBoard);
                    Piece copyPiece = findPiece(newBoard, piece.getSymbol());

                    newBoard = copyPiece.move(newBoard, forward);

                    BoardState newBoardState = new BoardState(newBoard, pathCost + 1, this);

                    if(!newBoardState.getBoard().equalTo(currentBoard)){
                        path.add(newBoardState);
                    }
                }
            }
        }
        return path;
    }
}
