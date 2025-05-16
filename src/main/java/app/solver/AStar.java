package app.solver;

import app.components.*;
import java.util.*;

public class AStar {
    ArrayList<Board> boardChain;

    public AStar() {
        this.boardChain = new ArrayList<Board>();
    }

    public ArrayList<Board> aStarSolver(BoardState object) {
        boardChain.add(object.getBoard());
        return this.boardChain;
    }
}
