package app.solver;

import app.components.*;
import java.util.*;

public class AStar {
    ArrayList<Board> boardChain;
    private int totalNodes;
    private String heuristicType = "Distance to Exit";

    public AStar() {
        this.boardChain = new ArrayList<Board>();
    }

    public int getTotalNodes() {
        return this.totalNodes;
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

    public ArrayList<Board> aStarSolver(BoardState startState) {
        this.totalNodes = 0;
        PriorityQueue<BoardState> queue;

        if ("Blocking Vehicles".equals(heuristicType)) {
            queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getAStarCost));
        } else if ("Combined".equals(heuristicType)) {
            queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::calculateAStarCombinedCost));
        } else {
            queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getAStarDistanceCost));
        }
        
        Set<String> visited = new HashSet<>();
        queue.add(startState);

        while (!queue.isEmpty()) {
            BoardState currentNode = queue.poll();
            Board board = currentNode.getBoard();

            String signature = boardSignature(board);
            if (visited.contains(signature)) {
                continue;
            }
            visited.add(signature);
            this.totalNodes++;

            if (board.isSolved()) {
                ArrayList<Board> result = new ArrayList<>();
                while (currentNode != null) {
                    result.add(0, currentNode.getBoard());
                    currentNode = currentNode.getParent();
                }
                return result;
            }

            for (BoardState neighbor : currentNode.generatePath()) {
                queue.add(neighbor);
            }
        }

        return new ArrayList<>();
    }
}
