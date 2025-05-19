package app.solver;

import app.components.*;
import java.util.*;
// import java.util.function.Function;

public class GreedyBFS {
    ArrayList<Board> boardChain;
    private int totalNodes;
    private String heuristicType = "Distance to Exit";
    
    public GreedyBFS() {
        this.boardChain = new ArrayList<Board>();
        this.totalNodes = 0;
    }
    
    public int getTotalNodes() {
        return totalNodes;
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
        totalNodes = 0;
        PriorityQueue<BoardState> queue;

        if ("Blocking Vehicles".equals(heuristicType)) {
            queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getCost));
        } else if ("Combined".equals(heuristicType)) {
            queue = new PriorityQueue<>(Comparator.comparingInt(state -> state.getBoard().calculateCombinedCost()));
        } else {
            queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getDistanceCost));
        }
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
            totalNodes++;

            if (board.isSolved()) {
                ArrayList<Board> result = new ArrayList<>();
                while (current != null) {
                    result.add(0, current.getBoard());
                    current = current.getParent();
                }
                return result;
            }

            // Harus ditinjau ulang cara kerja GBFS.
            while (!queue.isEmpty()) {
                queue.poll();
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