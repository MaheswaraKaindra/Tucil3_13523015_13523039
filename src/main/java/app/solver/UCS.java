package app.solver;

import app.components.*;
import java.util.*;

public class UCS {
    ArrayList<Board> boardChain;
    
    public UCS() {
        this.boardChain = new ArrayList<>();
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
        PriorityQueue<BoardState> queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getPathCost));
        Set<String> visited = new HashSet<>();

        queue.add(object);
        
        while(!queue.isEmpty()){
            BoardState current = queue.poll();
            Board currentBoard = current.getBoard();

            String signature = boardSignature(currentBoard);
            if(visited.contains(signature)){
                continue;
            }
            visited.add(signature);

            if(currentBoard.isSolved()){
                buildBoardChain(current);
                break;
            }

            //currentBoard.displayBoard();
            for(BoardState next : current.generatePath()){
                //next.getBoard().displayBoard();
                //System.out.println();
                queue.add(next);
            }
        }
        return this.boardChain;
    }
}
