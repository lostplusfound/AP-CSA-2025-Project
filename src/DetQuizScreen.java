import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class DetQuizScreen {
    private static int questionCount = 1;
    private static int correctQuestions = 0;
    private static Matrix currentMatrix;

    public static Scene createQuizScene(Stage primaryStage, Module module) {
        Label questionLabel = new Label(questionCount + ". What is the determinant of this matrix?");
        
        int size = switch (module.getDifficulty()) {
            case "Easy" -> 2;
            case "Medium" -> 3;
            case "Hard" -> 4;
            default -> throw new IllegalArgumentException("Invalid difficulty");
        };

        // Create matrix grid
        GridPane matrixGrid = new GridPane();
        currentMatrix = Module.generateMatrix(size, size);
        updateQuestion(matrixGrid, currentMatrix, questionLabel);
        matrixGrid.setHgap(10);
        matrixGrid.setVgap(10);
        matrixGrid.setStyle("-fx-alignment: center;");

        TextField answerBox = new TextField();
        answerBox.setPromptText("Enter answer");
        answerBox.setMaxWidth(200);

        Label feedbackLabel = new Label();

        Button submitButton = new Button("Submit Answer");
        submitButton.setStyle("-fx-background-color: #66cc66; -fx-text-fill: white;");

        Button nextButton = new Button("Next");
        nextButton.setDisable(true);

        Button backButton = new Button("Exit quiz");
        backButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
        
        submitButton.setOnAction(e -> {
            try {
                double userAnswer = Double.parseDouble(answerBox.getText().trim());
                double correctAnswer = currentMatrix.findDet();

                if (Math.abs(userAnswer - correctAnswer) < 0.0001) { // Allow small floating-point errors
                    feedbackLabel.setText("Correct!");
                    submitButton.setDisable(true);
                    nextButton.setDisable(false);
                    correctQuestions++;
                    
                } else {
                    feedbackLabel.setText("Incorrect. The correct answer is: " + correctAnswer);
                    submitButton.setDisable(true);
                    nextButton.setDisable(false);
                }
            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Invalid input. Please enter a number.");
            }
        });

        nextButton.setOnAction(e -> {
            submitButton.setDisable(false);
            nextButton.setDisable(true);
            questionCount++;

            if (questionCount <= module.getNumQuestions()) {
                currentMatrix = Module.generateMatrix(size, size);
                updateQuestion(matrixGrid, currentMatrix, questionLabel);
                feedbackLabel.setText("");
                answerBox.clear();
            } else {
                questionLabel.setText("Quiz complete! You scored " + (correctQuestions / (double) module.getNumQuestions()) * 100 + "%");
                matrixGrid.setVisible(false);
                answerBox.setVisible(false);
                submitButton.setVisible(false);
                nextButton.setVisible(false);
                feedbackLabel.setText("");
                questionCount = 1;
                correctQuestions = 0;
            }
        });
        

        // Button to return to the home screen
        backButton.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Are you sure you want to quit? Your progress will not be saved.");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                new HomeScreen().start(primaryStage);
            }
        });

        // Set up the layout
        HBox buttonBox = new HBox(10, submitButton, nextButton);
        buttonBox.setStyle("-fx-alignment: center;");
        VBox layout = new VBox(20, questionLabel, matrixGrid, answerBox, feedbackLabel, buttonBox, backButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(layout, 400, 400);
    }

    private static void updateQuestion(GridPane matrixGrid, Matrix matrix, Label question) {
        question.setText(questionCount + ". What is the determinant of this matrix?");
        matrixGrid.getChildren().clear();
        double[][] values = matrix.getValues();

        // Create a solid left border (Rectangle)
        Rectangle leftBorder = new Rectangle(1, values.length * 50);
        leftBorder.setStyle("-fx-fill: black;");
        matrixGrid.add(leftBorder, 0, 0, 1, values.length);

        // Create matrix grid
        for (int row = 0; row < values.length; row++) {
            for (int col = 0; col < values[row].length; col++) {
                Label label = new Label(String.valueOf(values[row][col]));
                label.setStyle("-fx-padding: 10px; -fx-font-size: 16px; -fx-text-alignment: center;");
                matrixGrid.add(label, col + 1, row);
            }
        }

        // Create a solid right border (Rectangle)
        Rectangle rightBorder = new Rectangle(1, values.length * 50);
        rightBorder.setStyle("-fx-fill: black;");
        matrixGrid.add(rightBorder, values[0].length + 1, 0, 1, values.length);
    }
}
