package app.solver;

import app.components.*;
import java.util.*;

public class GreedyBFS {
    ArrayList<Board> boardChain;
    
    public GreedyBFS() {

    }

    public ArrayList<Board> greedyBFSSolver(BoardState object) {
        boardChain.add(object.getBoard());
        return this.boardChain;
    }
}