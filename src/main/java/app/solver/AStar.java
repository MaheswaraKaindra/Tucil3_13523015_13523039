package app.solver;

import app.components.*;
import java.util.*;

public class AStar {
    ArrayList<Board> boardState;

    public AStar() {
        this.boardState = new ArrayList<Board>();
    }

    public ArrayList<Board> aStarSolver(BoardState object) {
        boardState.add(object.getBoard());
        return this.boardState;
    }
}
