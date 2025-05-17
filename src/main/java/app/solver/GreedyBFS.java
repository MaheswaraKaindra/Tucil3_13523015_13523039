package app.solver;

import app.components.*;
import java.util.*;
import java.util.function.ToIntFunction;

public class GreedyBFS {
    ArrayList<Board> boardChain;
    private String heuristicType;
    
    public GreedyBFS() {
        this.boardChain = new ArrayList<Board>();
        this.heuristicType = "Distance to Exit";
    }
    
    public void setHeuristic(String heuristicType) {
        this.heuristicType = heuristicType;
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

    public ArrayList<Board> greedyBFSSolver(BoardState startState) {
        ToIntFunction<BoardState> heuristicFunction;
        
        switch (heuristicType) {
            case "Distance to Exit":
                heuristicFunction = BoardState::getDistanceCost;
                break;
            case "Blocking Vehicles":
                heuristicFunction = BoardState::getCost;
                break;
            case "Combined":
                heuristicFunction = boardState -> 
                    boardState.getBoard().calculateCombinedCost();
                break;
            default:
                heuristicFunction = BoardState::getDistanceCost;
        }
        
        PriorityQueue<BoardState> queue = new PriorityQueue<>(Comparator.comparingInt(heuristicFunction));
        Set<String> visited = new HashSet<>();

        queue.add(startState);

        while (!queue.isEmpty()) {
            BoardState current = queue.poll();
            Board board = current.getBoard();

            String signature = boardSignature(board);
            if(visited.contains(signature)){
                continue;
            }
            visited.add(signature);

            if (board.isSolved()) {
                ArrayList<Board> result = new ArrayList<>();
                while (current != null) {
                    result.add(0, current.getBoard());
                    current = current.getParent();
                }
                return result;
            }

            for (BoardState neighbor : current.generatePath()) {
                if (!visited.contains(boardSignature(neighbor.getBoard()))) {
                    queue.add(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }
}