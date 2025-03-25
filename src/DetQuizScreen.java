import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class DetQuizScreen {
    private static int questionNum = 1;
    private static int correctQuestions = 0;
    private static Matrix currentMatrix;
    private static GridPane matrixGrid;
    private static Label questionLabel;
    private static TextField answerBox;
    private static Label feedbackLabel;
    private static Button submitButton;
    private static Button nextButton; 
    private static Button exitButton; 
    private static int size;

    public static Scene createQuizScene(Stage primaryStage, Module module) {
        questionNum = 1;
        correctQuestions = 0;
        size = switch (module.getDifficulty()) {
            case "Easy" -> 2;
            case "Medium" -> 3;
            case "Hard" -> 4;
            default -> throw new IllegalArgumentException("Invalid difficulty");
        };
        matrixGrid = new GridPane(); 
        questionLabel = new Label(questionNum + ". What is the determinant of this matrix?");
        answerBox = new TextField();
        feedbackLabel = new Label();
        submitButton = new Button("Submit Answer");
        nextButton = new Button("Next");
        exitButton = new Button("Exit quiz");

        matrixGrid.getStyleClass().add("matrix-grid");
        currentMatrix = Module.generateMatrix(size, size);
        updateQuestion(currentMatrix);

        questionLabel.getStyleClass().add("question-label");
        feedbackLabel.getStyleClass().add("feedback-label");


        answerBox.setPromptText("Enter answer");
        answerBox.setMaxWidth(200);
        answerBox.getStyleClass().add("answer-box");
        
        submitButton.getStyleClass().add("submit-button");
        nextButton.setDisable(true);
        nextButton.getStyleClass().add("next-button");
        exitButton.getStyleClass().add("exit-button");

        submitButton.setOnAction(e -> handleSubmit());
        nextButton.setOnAction(e -> handleNext(module, size));
        exitButton.setOnAction(e -> handleBack(primaryStage));

        HBox buttonBox = new HBox(10, submitButton, nextButton);
        buttonBox.getStyleClass().add("button-box");
        VBox layout = new VBox(20, questionLabel, matrixGrid, answerBox, feedbackLabel, buttonBox, exitButton);
        layout.getStyleClass().add("quiz-layout");
        layout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));


        Scene detQuizScene = new Scene(layout, 400, 400);
        detQuizScene.getStylesheets().add("styles.css");
        detQuizScene.setFill(Color.BLACK);

        return detQuizScene;
    }

    private static void handleSubmit() {
        try {
            double userAnswer = Double.parseDouble(answerBox.getText().trim());
            double correctAnswer = currentMatrix.findDet();

            if (Math.abs(userAnswer - correctAnswer) < 0.0001) {
                feedbackLabel.setStyle("-fx-text-fill: #00ff08;");
                feedbackLabel.setText("Correct!");
                submitButton.setDisable(true);
                nextButton.setDisable(false);
                correctQuestions++;
            } else {
                feedbackLabel.setStyle("-fx-text-fill:rgb(255, 0, 0);");
                feedbackLabel.setText("Incorrect. The correct answer is: " + correctAnswer);
                submitButton.setDisable(true);
                nextButton.setDisable(false);
            }
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Invalid input. Please enter a number.");
        }
    }

    private static void handleNext(Module module, int size) {
        submitButton.setDisable(false);
        nextButton.setDisable(true);
        questionNum++;

        if (questionNum <= module.getNumQuestions()) {
            currentMatrix = Module.generateMatrix(size, size);
            updateQuestion(currentMatrix);
            feedbackLabel.setText("");
            answerBox.clear();
            questionLabel.setText(questionNum + ". What is the determinant of this matrix?");
        } else {
            questionLabel.setText("Quiz complete! You scored " + (correctQuestions / (double) module.getNumQuestions()) * 100 + "%");
            matrixGrid.setVisible(false);
            answerBox.setVisible(false);
            submitButton.setVisible(false);
            nextButton.setVisible(false);
            feedbackLabel.setText("");
        }
    }

    private static void handleBack(Stage primaryStage) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Are you sure you want to quit? Your progress will not be saved.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new HomeScreen().start(primaryStage);
        }
    }

    private static void updateQuestion(Matrix matrix) {
        matrixGrid.getChildren().clear();
        double[][] values = matrix.getValues();

        Rectangle leftBorder = new Rectangle(1, values.length * 50);
        leftBorder.getStyleClass().add("matrix-border");
        matrixGrid.add(leftBorder, 0, 0, 1, values.length);

        for (int row = 0; row < values.length; row++) {
            for (int col = 0; col < values[row].length; col++) {
                Label label = new Label(String.valueOf(values[row][col]));
                label.getStyleClass().add("matrix-cell");
                matrixGrid.add(label, col + 1, row);
            }
        }

        Rectangle rightBorder = new Rectangle(1, values.length * 50);
        rightBorder.getStyleClass().add("matrix-border");
        matrixGrid.add(rightBorder, values[0].length + 1, 0, 1, values.length);
    }
}
