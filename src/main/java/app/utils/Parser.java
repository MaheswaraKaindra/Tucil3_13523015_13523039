package app.utils;

import app.components.*;

import java.io.*;
import java.util.*;

public class Parser {    
    private static String inferExitDirection(int row, int col, int maxRow, int maxCol ){
        if (col >= maxCol) return Exit.RIGHT; 
        if (col < 0) return Exit.LEFT;    
        if (row >= maxRow) return Exit.DOWN; 
        if (row < 0) return Exit.UP;     
        
        throw new IllegalArgumentException("Invalid exit position: (" + row + ", " + col + ")");
    }

    public static BoardState parse(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String[] lines = reader.readLine().split(" ");
        int rows = Integer.parseInt(lines[0]);
        int cols = Integer.parseInt(lines[1]);

        Board board = new Board(rows, cols);
        int n = Integer.parseInt(reader.readLine());

        char[][] grid = new char[rows][cols];
        int exitRow = -1;
        int exitCol = -1;
        int currentRow = 0;
        boolean topExit = false;
        Set<Character> seen = new HashSet<>();
        String line;
        while((line = reader.readLine()) != null){
            if(currentRow >= rows){
                // Case: Bottom exit
                for(int c = 0; c < line.length(); c++){
                    if (line.charAt(c) == 'K') {
                        exitRow = rows;
                        exitCol = c;
                    }
                }
                continue;
            }

            if(currentRow == 0 && line.contains("K") && !topExit){
                // Case: top exit
                for(int c = 0; c < line.length(); c++){
                    if(line.charAt(c) == 'K'){
                        exitRow = -1;
                        exitCol = c;
                        topExit = true;
                    }
                }
                continue;
            }

            int colStart = 0;
            if(line.length() > 0 && line.charAt(0) == 'K'){
                // Case: left exit
                exitRow = currentRow;
                exitCol = -1;
                colStart = 1; 
            }

            if(line.length() > cols && line.charAt(cols) == 'K'){
                // Case: right exit
                exitRow = currentRow;
                exitCol = cols;
            }

            for(int c = 0; c < Math.min(cols + colStart, line.length()); c++){
                char ch = line.charAt(c);
                if (ch != 'K') {
                    int gridCol = c - colStart;
                    grid[currentRow][gridCol] = ch ;
                    board.setCell(currentRow, gridCol, new Cell(currentRow, gridCol, ch));
                }
            }
            currentRow++;
        }
        reader.close();

        if (exitRow == -1 && exitCol == -1) {
            throw new IllegalArgumentException("Exit 'K' not found in the input.");
        }

        String direction = inferExitDirection(exitRow, exitCol, rows, cols);
        board.setExit(new Exit(exitRow, exitCol, direction));

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                char cellChar = grid[r][c];

                if(cellChar == '.' || cellChar == 'K' || seen.contains(cellChar)){
                    continue;
                }

                seen.add(cellChar);

                boolean isHorizontal = (c + 1 < cols && grid[r][c + 1] == cellChar);
                boolean isVertical = (r + 1 < rows && grid[r + 1][c] == cellChar);
                int length = 1;

                if(isHorizontal && isVertical){
                    throw new IllegalArgumentException("Invalid cell configuration at (" + r + ", " + c + ")");
                }
                if(isHorizontal){
                    int nextCol = c + 1;
                    while(nextCol < cols && grid[r][nextCol] == cellChar){
                        length++;
                        nextCol++;
                    }
                } else if(isVertical){
                    int nextRow = r + 1;
                    while(nextRow < rows && grid[nextRow][c] == cellChar){
                        length++;
                        nextRow++;
                    }
                }

                if(cellChar == 'P'){
                    board.addPrimaryPiece(new PrimaryPiece(r, c, cellChar, length, isHorizontal));
                } else{
                    board.addPiece(new Piece(r, c, cellChar, length, isHorizontal));
                }
            }
        }
        if (board.getPrimaryPiece() == null) {
            throw new IllegalArgumentException("Primary piece 'P' not found in the board.");
        }
        int actualPieceCount = board.getPieces().size();
        if (actualPieceCount != n) {
            throw new IllegalArgumentException("Piece count mismatch: expected " + n + " but found " + actualPieceCount);
        }

        BoardState returnValue = new BoardState(board, board.calculateCost(), 0, null);
        return returnValue;
    }     
}
