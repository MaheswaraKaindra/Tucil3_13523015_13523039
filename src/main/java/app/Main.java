package app;

import app.components.*;
import app.solver.UCS;
import app.utils.Parser;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            String filePath = "C:\\Users\\ASUS\\Documents\\SEMESTER_4\\STRATEGI_ALGORITMA\\TUCIL_3\\Tucil3_13523015_13523039\\test\\test.txt";

            BoardState startState = Parser.parse(filePath);
            startState.getBoard().displayBoard();

            UCS solver = new UCS();
            ArrayList<Board> solution = solver.ucssolver(startState);

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
