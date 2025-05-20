package app;

import app.components.*;
import app.solver.AStar;
import app.solver.GreedyBFS;
// import app.solver.UCS;
import app.utils.Parser;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Working directory: " + new java.io.File(".").getAbsolutePath());
            String filePath = "test/test.txt";

            BoardState startState = Parser.parse(filePath);
            startState.getBoard().displayBoard();

            AStar solver = new AStar();
            ArrayList<Board> solution = solver.aStarSolver(startState);

            if (solution.isEmpty()) {
                System.out.println("No solution found.");
            } else {
                System.out.println("Solution found in " + (solution.size() - 1) + " moves:");
                int step = 0;
                for (Board board : solution) {
                    System.out.println("\nStep " + step++);
                    board.displayBoard();
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
