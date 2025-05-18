package app.controller;

import app.components.*;
import app.utils.Parser;
import app.solver.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.net.URL;

public class RushHourController implements Initializable {    
    @FXML private GridPane boardGridPane;
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private ComboBox<String> heuristicComboBox;
    @FXML private Label heuristicLabel;
    @FXML private Label statusLabel;
    @FXML private Button saveSolutionButton;
    @FXML private GridPane statsGridPane;
    @FXML private Label algorithmLabel;
    @FXML private Label heuristicValueLabel;
    @FXML private Label timeLabel;
    @FXML private Label nodesLabel;
    
    // Mapping warna
    private final Map<Character, Color> pieceColors = new HashMap<>();
    
    private Board currentBoard;
    private static final int CELL_SIZE = 60;
    private ArrayList<Board> currentSolution;
    private long executionTime;
    private int totalNodesExpanded;
    private String algorithmUsed;
    private String heuristicUsed;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        algorithmComboBox.setItems(FXCollections.observableArrayList(
            "UCS", "A*", "Greedy Best-First"
        ));
        algorithmComboBox.getSelectionModel().selectFirst();
        
        heuristicComboBox.setItems(FXCollections.observableArrayList(
            "Distance to Exit", "Blocking Vehicles", "Combined"
        ));
        heuristicComboBox.getSelectionModel().selectFirst();
        
        heuristicLabel.setVisible(false);
        heuristicComboBox.setVisible(false);
        
        algorithmComboBox.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                boolean isAStar = "A*".equals(newValue) || "Greedy Best-First".equals(newValue);
                heuristicLabel.setVisible(isAStar);
                heuristicComboBox.setVisible(isAStar);
            }
        );
        initializePieceColors();
    }
    
    private void initializePieceColors() {
        pieceColors.put('P', Color.RED);
        
        pieceColors.put('A', Color.BLUE);
        pieceColors.put('B', Color.GREEN);
        pieceColors.put('C', Color.ORANGE);
        pieceColors.put('D', Color.PURPLE);
        pieceColors.put('E', Color.TEAL);
        pieceColors.put('F', Color.PINK);
        pieceColors.put('G', Color.DARKBLUE);
        pieceColors.put('H', Color.DARKGREEN);
        pieceColors.put('I', Color.DARKORANGE);
        pieceColors.put('J', Color.DARKRED);
        pieceColors.put('K', Color.DARKVIOLET);
        pieceColors.put('L', Color.DARKCYAN);
        pieceColors.put('M', Color.MAGENTA);
        pieceColors.put('N', Color.NAVY);
        pieceColors.put('O', Color.OLIVE);
        
        pieceColors.put('.', Color.LIGHTGRAY);
    }

    @FXML
    private void handleLoadPuzzle() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        
        File selectedFile = fileChooser.showOpenDialog(boardGridPane.getScene().getWindow());
        if (selectedFile != null) {
            try {
                BoardState boardState = Parser.parse(selectedFile);

                currentBoard = boardState.getBoard();
                renderBoard(currentBoard);
                statusLabel.setText("Puzzle loaded: " + selectedFile.getName());
                
            } catch (IOException e) {
                showAlert("Error loading file: " + e.getMessage());
            } catch (NumberFormatException e) {
                showAlert("Invalid file format. Expected dimensions in first line.");
            } catch (Exception e) {
                showAlert("Error: " + e.getMessage());
            }
        }
    }
    @FXML
    private void handleSolve() {
        if (currentBoard == null) {
            showAlert("Please load a puzzle first.");
            return;
        }
        
        String algorithm = algorithmComboBox.getValue();
        String heuristic = heuristicComboBox.getValue();
        
        statusLabel.setText("Solving with " + algorithm + "...");
        
        BoardState initialState = new BoardState(currentBoard, 0, null);

        ArrayList<Board> solution = null;
        totalNodesExpanded = 0;
        long startTime = System.currentTimeMillis();
        
        try {
            switch (algorithm) {
                case "UCS":
                    UCS ucs = new UCS();
                    solution = ucs.ucsSolver(initialState);
                    algorithmUsed = "UCS";
                    heuristicUsed = "N/A";
                    totalNodesExpanded = ucs.getTotalNodes();
                    break;                
                case "A*":
                    AStar aStar = new AStar();
                    aStar.setHeuristic(heuristic);
                    solution = aStar.aStarSolver(initialState);
                    totalNodesExpanded = aStar.getTotalNodes();
                    algorithmUsed = "A*";
                    heuristicUsed = heuristic;
                    break;
                case "Greedy Best-First":
                    GreedyBFS greedy = new GreedyBFS();
                    greedy.setHeuristic(heuristic);
                    solution = greedy.greedyBFSSolver(initialState);
                    totalNodesExpanded = greedy.getTotalNodes();
                    algorithmUsed = "Greedy Best-First";
                    heuristicUsed = heuristic;
                    break;
                default:
                    showAlert("Unknown algorithm: " + algorithm);
                    return;
            }
              long endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;
            
            if (solution == null || solution.isEmpty()) {
                statusLabel.setText("No solution found!");
                saveSolutionButton.setDisable(true);
                statsGridPane.setVisible(false);
            } else {
                statusLabel.setText(String.format("Solution found in %d steps! Time: %d ms, Nodes expanded: %d", 
                    (solution.size()-1), executionTime, totalNodesExpanded));
                currentSolution = new ArrayList<>(solution);
                saveSolutionButton.setDisable(false);

                algorithmLabel.setText(algorithmUsed);
                heuristicValueLabel.setText(heuristicUsed);
                timeLabel.setText(executionTime + " ms");
                nodesLabel.setText(String.valueOf(totalNodesExpanded));
                statsGridPane.setVisible(true);
                
                simulateSolution(solution);
            }
        } catch (Exception e) {
            showAlert("Error during solving: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void simulateSolution(List<Board> solution) {
        if (solution == null || solution.isEmpty()) {
            return;
        }
        
        final int[] currentStepIndex = {0};
        Timeline timeline = new Timeline();
        
        for (int i = 0; i < solution.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.75), event -> {
                Board board = solution.get(stepIndex);
                renderBoard(board);
                currentStepIndex[0] = stepIndex;
                
                statusLabel.setText("Step " + stepIndex + " of " + (solution.size()-1));
                
                if (board.isSolved()) {
                    statusLabel.setText("Puzzle solved in " + (solution.size()-1) + " steps!");
                }
            });
            
            timeline.getKeyFrames().add(keyFrame);
        }
        
        timeline.play();
    }
    
    public void renderBoard(Board board) {
        if (board == null) return;
        
        boardGridPane.getChildren().clear();
        
        int rows = board.getRows();
        int cols = board.getCols();
        
        boardGridPane.getColumnConstraints().clear();
        boardGridPane.getRowConstraints().clear();
        
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(CELL_SIZE);
            colConstraints.setPrefWidth(CELL_SIZE);
            boardGridPane.getColumnConstraints().add(colConstraints);
        }
        
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(CELL_SIZE);
            rowConstraints.setPrefHeight(CELL_SIZE);
            boardGridPane.getRowConstraints().add(rowConstraints);
        }
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                app.components.Cell cell = board.getCell(row, col);
                char cellValue = cell.getSymbol();
                
                StackPane cellPane = new StackPane();
                cellPane.getStyleClass().add("cell");
                
                // Set warna
                Color pieceColor = pieceColors.getOrDefault(cellValue, Color.GRAY);
                cellPane.setStyle(String.format(
                    "-fx-background-color: %s; -fx-border-color: black; -fx-border-width: 1px;",
                    toRgbString(pieceColor)
                ));
                
                if (cellValue != '.') {
                    Label label = new Label(String.valueOf(cellValue));
                    label.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
                    cellPane.getChildren().add(label);
                }
                
                boardGridPane.add(cellPane, col, row);
            }
        }
        
        Exit exit = board.getExit();
        if (exit != null) {
            int exitRow = exit.getRow();
            int exitCol = exit.getCol();

            StackPane exitMarker = new StackPane();
            exitMarker.getStyleClass().add("exit-marker");
            
            // Exit marker
            if (exitRow >= rows) {
                boardGridPane.add(exitMarker, exitCol, rows - 1);
            } else if (exitRow < 0) {
                boardGridPane.add(exitMarker, exitCol, 0);
            } else if (exitCol >= cols) {
                boardGridPane.add(exitMarker, cols - 1, exitRow);
            } else if (exitCol < 0) {
                boardGridPane.add(exitMarker, 0, exitRow);
            }
        }

        highlightPieces(board);
    }
    
    private void highlightPieces(Board board) {
        for (Piece piece : board.getPieces()) {
            char symbol = piece.getSymbol();
            Color color = pieceColors.getOrDefault(symbol, Color.GRAY);
            String colorStyle = String.format(
                "-fx-background-color: %s; -fx-border-color: black; -fx-border-width: 1px;",
                toRgbString(color)
            );

            int[][] positions = piece.getFullPosition();
            
            for (int[] position : positions) {
                int row = position[0];
                int col = position[1];
                
                if (row < 0 || row >= board.getRows() || col < 0 || col >= board.getCols()) {
                    continue;
                }

                for (javafx.scene.Node node : boardGridPane.getChildren()) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                        node.setStyle(colorStyle);
                        
                        if (node instanceof StackPane) {
                            StackPane cellPane = (StackPane) node;
                            cellPane.getChildren().clear();
                            
                            // nama piece
                            //Label label = new Label(String.valueOf(symbol));
                            //label.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
                            //cellPane.getChildren().add(label);
                        }
                        
                        break;
                    }
                }
            }
        }
    }
    
    private String toRgbString(Color color) {
        return String.format(
            "rgb(%d, %d, %d)",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255)
        );
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSaveSolution() {
        if (currentSolution == null || currentSolution.isEmpty()) {
            showAlert("No solution to save. Please solve the puzzle first.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Solution");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        
        File selectedFile = fileChooser.showSaveDialog(boardGridPane.getScene().getWindow());
        if (selectedFile != null) {
            try (java.io.PrintWriter writer = new java.io.PrintWriter(selectedFile)) {  
                writer.println("Algorithm: " + algorithmUsed);
                if (!"N/A".equals(heuristicUsed)) {
                    writer.println("Heuristic: " + heuristicUsed);
                }
                writer.println("Execution Time: " + executionTime + " ms");
                writer.println("Nodes Expanded: " + totalNodesExpanded);
                writer.println("Solution Steps: " + (currentSolution.size()-1)); 
                writer.println();            
                int step = 0;
                for (Board board : currentSolution) {
                    writer.println("Step " + step++);
                    
                    for (int r = 0; r < board.getRows(); r++) {
                        for (int c = 0; c < board.getCols(); c++) {
                            writer.print(board.getCell(r, c).getSymbol());
                        }
                        writer.println();
                    }
                    writer.println("Exit: " + board.getExit().getRow() + ", " + board.getExit().getCol());
                    writer.println();
                }
                
                statusLabel.setText("Solution saved to " + selectedFile.getName());
            } catch (IOException e) {
                showAlert("Error saving solution: " + e.getMessage());
            }
        }
    }
}