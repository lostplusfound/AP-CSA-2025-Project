import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load the background image

        // UI Elements
        Label title = new Label("Welcome to the Matrix");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Customize your quiz");
        subtitle.getStyleClass().add("subtitle");

        // Create dropdowns for quiz type
        ChoiceBox<String> quizTypeChoiceBox = new ChoiceBox<>();
        quizTypeChoiceBox.getItems().addAll("Determinant", "Multiplication", "Systems");
        quizTypeChoiceBox.setValue("Determinant");
        quizTypeChoiceBox.getStyleClass().add("dropdown");

        // Dropdown for difficulty
        ChoiceBox<String> difficultyChoiceBox = new ChoiceBox<>();
        difficultyChoiceBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyChoiceBox.setValue("Medium");
        difficultyChoiceBox.getStyleClass().add("dropdown");

        // Create a TextField for the number of questions
        TextField numQuestionsTextField = new TextField();
        numQuestionsTextField.setPromptText("Enter number of questions");
        numQuestionsTextField.getStyleClass().add("input-field");

        // Create Start Quiz Button
        Button startQuizButton = new Button("Start Quiz");
        startQuizButton.getStyleClass().add("start-button");

        // Event for start quiz btn
        startQuizButton.setOnAction(e -> {
            // Retrieve selected values
            String selectedQuizType = quizTypeChoiceBox.getValue();
            String selectedDifficulty = difficultyChoiceBox.getValue();
            String numQuestionsInput = numQuestionsTextField.getText();

            try {
                int numQuestions = Integer.parseInt(numQuestionsInput); 
                Module selectedModule = new Module(selectedQuizType, numQuestions, selectedDifficulty);
                System.out.println("Starting " + selectedQuizType + " Quiz with " + numQuestions + " questions at " + selectedDifficulty + " difficulty.");
                switch(selectedQuizType){
                    case "Determinant": 
                        primaryStage.setScene(DetQuizScreen.createQuizScene(primaryStage, selectedModule));
                        primaryStage.setMaximized(true);
                        break;
                    case "Multiplication":
                        break;
                    case "Systems": 
                        primaryStage.setScene(SystemsQuizScreen.getScene(primaryStage, selectedModule));
                        primaryStage.setMaximized(true);
                }
            } catch (NumberFormatException ex) {
                // Handle invalid number input
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Please enter a valid number of questions.");
                a.show();
            }
        });

        VBox layout = new VBox(10, title, subtitle, quizTypeChoiceBox, difficultyChoiceBox, numQuestionsTextField, startQuizButton);
        layout.getStyleClass().add("layout");

        StackPane root = new StackPane(layout);
        root.getStyleClass().add("home-screen");

        Scene homeScene = new Scene(root, 600, 600);
        homeScene.getStylesheets().add("styles.css");
        primaryStage.setMaximized(true);
        primaryStage.setTitle("The Matrix Quiz");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
