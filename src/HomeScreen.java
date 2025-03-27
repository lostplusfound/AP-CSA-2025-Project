import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class HomeScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label title = new Label("Welcome to the Matrix");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Customize your quiz");
        subtitle.getStyleClass().add("subtitle");

        ChoiceBox<String> quizTypeChoiceBox = new ChoiceBox<>();
        quizTypeChoiceBox.getItems().addAll("Determinant", "Multiplication", "Systems");
        quizTypeChoiceBox.setValue("Determinant");
        quizTypeChoiceBox.getStyleClass().add("dropdown");

        ChoiceBox<String> difficultyChoiceBox = new ChoiceBox<>();
        difficultyChoiceBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyChoiceBox.setValue("Medium");
        difficultyChoiceBox.getStyleClass().add("dropdown");

        TextField numQuestionsTextField = new TextField();
        numQuestionsTextField.setPromptText("Enter number of questions");
        numQuestionsTextField.getStyleClass().add("input-field");

        Button startQuizButton = new Button("Start Quiz");
        startQuizButton.getStyleClass().add("start-button");

        startQuizButton.setOnAction(e -> {
            String selectedQuizType = quizTypeChoiceBox.getValue();
            String selectedDifficulty = difficultyChoiceBox.getValue();
            String numQuestionsInput = numQuestionsTextField.getText();

            try {
                int numQuestions = Integer.parseInt(numQuestionsInput);

                if (numQuestions <= 0) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("The number of questions must be a positive integer.");
                    a.show();
                    return;
                }

                Module selectedModule = new Module(selectedQuizType, numQuestions, selectedDifficulty);
                System.out.println("Starting " + selectedQuizType + " Quiz with " + numQuestions + " questions at " + selectedDifficulty + " difficulty.");
                primaryStage.setMaximized(true);


                switch(selectedQuizType){
                    case "Determinant":
                        primaryStage.setScene(DetQuizScreen.createQuizScene(primaryStage, selectedModule));
                        break;
                    case "Multiplication":
                        primaryStage.setScene(MultiplicationQuizScreen.createQuizScene(primaryStage, selectedModule)); 
                        break;
                    case "Systems":
                        primaryStage.setScene(SystemsQuizScreen.getScene(primaryStage, selectedModule));
                }
            } catch (NumberFormatException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Please enter a valid number of questions.");
                a.show();
            }
        });

        VBox layout = new VBox(10, title, subtitle, quizTypeChoiceBox, difficultyChoiceBox, numQuestionsTextField, startQuizButton);
        layout.getStyleClass().add("layout");

        StackPane root = new StackPane(layout);
        root.getStyleClass().add("home-screen");


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Ensure the stage is set to the correct size
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setMaximized(true);

        // Create the scene with correct size
        Scene homeScene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("The Matrix Quiz");
        homeScene.getStylesheets().add("styles.css");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
