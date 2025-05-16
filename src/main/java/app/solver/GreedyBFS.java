package app.solver;

import app.components.*;
import java.util.*;

public class GreedyBFS {
    ArrayList<Board> boardState;
    
    public GreedyBFS() {

    }

    public ArrayList<Board> greedyBFSSolver(BoardState object) {
        boardState.add(object.getBoard());
        return this.boardState;
    }
}