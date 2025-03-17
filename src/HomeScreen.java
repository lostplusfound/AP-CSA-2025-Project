import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomeScreen extends Application {
    @Override 
    public void start(Stage primaryStage) {
        Label title = new Label("Welcome to the Matrix");
        Button determinantButton = new Button("Determinant Quiz");
        Button multiplicationButton = new Button("Multiplication Quiz");
        Button systemsButton = new Button("Systems Quiz");
        HBox selectionRow = new HBox(determinantButton, multiplicationButton, systemsButton);
        VBox stack = new VBox(title, selectionRow);
        Scene scene = new Scene(stack, 300, 300);
        primaryStage.setTitle("The Matrix");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
