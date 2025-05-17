package app.solver;

import app.components.*;
import java.util.*;
import java.util.function.Function;

public class GreedyBFS {
    ArrayList<Board> boardChain;
    private String heuristicType = "Distance to Exit";
    
    public GreedyBFS() {
        this.boardChain = new ArrayList<Board>();
    }
    
    public void setHeuristic(String heuristicType) {
        this.heuristicType = heuristicType;
    }

    public ArrayList<Board> greedyBFSSolver(BoardState startState) {
        PriorityQueue<BoardState> queue;
        
        // Choose comparator based on heuristic type
        if ("Blocking Vehicles".equals(heuristicType)) {
            queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getCost));
        } else if ("Combined".equals(heuristicType)) {
            queue = new PriorityQueue<>(Comparator.comparingInt(state -> 
                state.getBoard().calculateCombinedCost()));
        } else {
            // Default to "Distance to Exit"
            queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getDistanceCost));
        }
        Set<Board> visited = new HashSet<>();

        queue.add(startState);

        while (!queue.isEmpty()) {
            BoardState current = queue.poll();
            Board board = current.getBoard();

            if (visited.contains(board)) continue;
            visited.add(board);
            boardChain.add(board);

            if (board.isSolved()) {
                ArrayList<Board> result = new ArrayList<>();
                while (current != null) {
                    result.add(0, current.getBoard());
                    current = current.getParent();
                }
                return result;
            }

            for (BoardState neighbor : current.generatePath()) {
                if (!visited.contains(neighbor.getBoard())) {
                    queue.add(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }
}