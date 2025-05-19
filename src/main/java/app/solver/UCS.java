package app.solver;

import app.components.*;
import java.util.*;

public class UCS {
    ArrayList<Board> boardChain;
    private int totalNodes;
    
    public UCS() {
        this.boardChain = new ArrayList<>();
        this.totalNodes = 0;
    }
    
    public int getTotalNodes() {
        return totalNodes;
    }

    private String boardSignature(Board board){
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                sb.append(board.getCell(r, c).getSymbol());
            }
        }
        return sb.toString();
    }

    private void buildBoardChain(BoardState goal){
        BoardState current = goal;
        while (current != null){
            boardChain.add(current.getBoard());
            current = current.getParent();
        }
        Collections.reverse(boardChain);
    }

    public ArrayList<Board> ucsSolver(BoardState object) {
        totalNodes = 0;
        PriorityQueue<BoardState> queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getPathCost));
        Set<String> visited = new HashSet<>();

        queue.add(object);
        
        while(!queue.isEmpty()){
            BoardState current = queue.poll();
            Board currentBoard = current.getBoard();

            totalNodes++;

            if(currentBoard.isSolved()){
                buildBoardChain(current);
                break;
            }

            String signature = boardSignature(currentBoard);
            if(visited.contains(signature)){
                continue;
            }
            visited.add(signature);

            for(BoardState next : current.generatePath()){
                queue.add(next);
            }
        }
        return this.boardChain;
    }
}
