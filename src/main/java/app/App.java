package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Resource: " + getClass().getResource("/layout.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/layout.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Rush Hour Solver");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
