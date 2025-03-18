import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title and subtitle
        Label title = new Label("Welcome to the Matrix");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;");

        Label subtitle = new Label("Customize your quiz");
        subtitle.setStyle("-fx-font-size: 16px; -fx-padding: 5; -fx-text-fill: gray;"); 

        // Create dropdowns for quiz type and difficulty
        ChoiceBox<String> quizTypeChoiceBox = new ChoiceBox<>();
        quizTypeChoiceBox.getItems().addAll("Determinant", "Multiplication", "Systems");
        quizTypeChoiceBox.setValue("Determinant");  // Default selection

        ChoiceBox<String> difficultyChoiceBox = new ChoiceBox<>();
        difficultyChoiceBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyChoiceBox.setValue("Medium");  // Default selection

        // Create a TextField for the number of questions
        TextField numQuestionsTextField = new TextField();
        numQuestionsTextField.setPromptText("Enter number of questions");
        numQuestionsTextField.setMaxWidth(500);

        // Create Start Quiz Button
        Button startQuizButton = new Button("Start Quiz");
        startQuizButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Event for start quiz btn
        startQuizButton.setOnAction(_ -> {
            // Retrieve selected values
            String selectedQuizType = quizTypeChoiceBox.getValue();
            String selectedDifficulty = difficultyChoiceBox.getValue();
            String numQuestionsInput = numQuestionsTextField.getText();

            try {
                int numQuestions = Integer.parseInt(numQuestionsInput);  
                Module.Difficulty difficulty = Module.Difficulty.valueOf(selectedDifficulty.toUpperCase());
                Module selectedModule = new Module(selectedQuizType, numQuestions, difficulty);
                System.out.println("Starting " + selectedQuizType + " Quiz with " + numQuestions + " questions at " + difficulty + " difficulty.");

            } catch (NumberFormatException ex) {
                // Handle invalid number input
                System.out.println("Please enter a valid number of questions.");
            }
        });

        // Layout: stack the elements vertically
        VBox layout = new VBox(10, title, subtitle, quizTypeChoiceBox, difficultyChoiceBox, numQuestionsTextField, startQuizButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Set up the scene and stage
        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setTitle("The Matrix Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
