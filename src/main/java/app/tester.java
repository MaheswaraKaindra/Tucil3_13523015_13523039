package app;

import app.components.BoardState;
import app.utils.Parser;

public class tester {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\ASUS\\Documents\\SEMESTER_4\\STRATEGI_ALGORITMA\\TUCIL_3\\Tucil3_13523015_13523039\\test\\test.txt";  // adjust to your actual file path

        try {
            BoardState board = Parser.parse(filePath);
            board.getBoard().displayBoard();
            System.out.println(board.getCost());
        } catch (Exception e) {
            System.err.println("Failed to parse board: " + e.getMessage());
        }
    }
}
