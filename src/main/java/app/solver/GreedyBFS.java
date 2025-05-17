package app.solver;

import app.components.*;
import java.util.*;

public class GreedyBFS {
    ArrayList<Board> boardChain;
    
    public GreedyBFS() {
        this.boardChain = new ArrayList<Board>();
    }

    public ArrayList<Board> greedyBFSSolver(BoardState startState) {
        PriorityQueue<BoardState> queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getDistanceCost));
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