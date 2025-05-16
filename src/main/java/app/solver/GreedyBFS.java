package app.solver;

import app.components.*;
import java.util.*;

public class GreedyBFS {
    ArrayList<Board> boardChain;
    
    public GreedyBFS() {
        this.boardChain = new ArrayList<Board>();
    }

    public ArrayList<Board> greedyBFSSolver(BoardState object) {
        BoardState currentNode = object;
        boardChain.add(currentNode.getBoard());

        List<BoardState> neighborList = currentNode.generatePath();
        while (currentNode.getBoard().isSolved()) {
            while (!neighborList.isEmpty()) {
                BoardState temp = neighborList.remove(0);
                if (temp.getParent() != currentNode && (temp.getCost() <= currentNode.getCost() && temp.getDistanceCost() <= currentNode.getDistanceCost())) {
                    currentNode = temp;
                }
            }
            boardChain.add(currentNode.getBoard());
            neighborList = currentNode.generatePath();
        }
        return this.boardChain;
    }
}