import java.util.Optional;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.shape.Line;

public class MultiplicationQuizScreen {
    private static Matrix currentMatrix1;
    private static Matrix currentMatrix2;

    private static Label questionLabel;

    private static Label feedbackLabel;
    private static Button nextButton;
    private static Button submitButton;

    private static GridPane matrix1GridPane;
    private static GridPane matrix2GridPane;
    private static GridPane inputGridPane = new GridPane();
    private static HBox inputMatrix;

    private static TextField[][] inputFields;

    private static HBox questionMatrices;

    private static String difficulty;
    private static int size;
    private static int numQuestions;
    private static int questionNum;
    private static int questionsCorrect = 0;

    public static Scene createQuizScene(Stage primaryStage, Module selectedModule) {
        difficulty = selectedModule.getDifficulty();
        questionNum = 1;
        numQuestions = selectedModule.getNumQuestions();
    
        if (difficulty.equals("Easy")) {
            size = 2;
        } else if (difficulty.equals("Medium")) {
            size = 3;
        } else {
            size = 4;
        }
    
        VBox layout = new VBox(10);
        layout.getStyleClass().add("quiz-layout");
    
        questionLabel = new Label((questionsCorrect + 1) + ". What is the product of these two matrices?");
        questionLabel.getStyleClass().add("question-label");
    
        feedbackLabel = new Label("");
        feedbackLabel.getStyleClass().add("feedback-label");
    
        inputFields = new TextField[size][size];
    
        currentMatrix1 = Module.generateMatrix(size, size);
        currentMatrix2 = Module.generateMatrix(size, size);
    
        matrix1GridPane = new GridPane();
        matrix2GridPane = new GridPane();
    
        fillMatrixGridPane(matrix1GridPane, currentMatrix1);
        fillMatrixGridPane(matrix2GridPane, currentMatrix2);
    
        inputGridPane.getChildren().clear();
        inputGridPane.getStyleClass().add("input-matrix-grid");
    
        // Create a container for the brackets and the input grid
        inputMatrix = new HBox(10);
        inputMatrix.getStyleClass().add("matrix-grid");
    
        // Left bracket for input matrix
        Group leftBracket = new Group();
        Line leftVertical = new Line(0, 0, 0, size * 60);
        Line leftTopHorizontal = new Line(0, 0, 10, 0);
        Line leftBottomHorizontal = new Line(0, size * 60, 10, size * 60);
    
        leftVertical.getStyleClass().add("input-matrix-border");
        leftTopHorizontal.getStyleClass().add("input-matrix-border");
        leftBottomHorizontal.getStyleClass().add("input-matrix-border");
    
        leftBracket.getChildren().addAll(leftVertical, leftTopHorizontal, leftBottomHorizontal);
    
        // Right bracket for input matrix
        Group rightBracket = new Group();
        Line rightVertical = new Line(0, 0, 0, size * 60);
        Line rightTopHorizontal = new Line(0, 0, -10, 0);
        Line rightBottomHorizontal = new Line(0, size * 60, -10, size * 60);
    
        rightVertical.getStyleClass().add("input-matrix-border");
        rightTopHorizontal.getStyleClass().add("input-matrix-border");
        rightBottomHorizontal.getStyleClass().add("input-matrix-border");
    
        rightBracket.getChildren().addAll(rightVertical, rightTopHorizontal, rightBottomHorizontal);
    
        // Input fields within the grid
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                inputFields[r][c] = new TextField();
                inputFields[r][c].getStyleClass().add("input-matrix-cell");
                inputGridPane.add(inputFields[r][c], c, r);  
            }
        }
    
        inputMatrix.getChildren().addAll(leftBracket, inputGridPane, rightBracket);
    
        // Button setup
        submitButton = new Button("Submit");
        submitButton.getStyleClass().add("submit-button");
        submitButton.setOnAction(e -> handleSubmit());
    
        nextButton = new Button("Next");
        nextButton.getStyleClass().add("next-button");
        nextButton.setOnAction(e -> handleNext(primaryStage));
        nextButton.setDisable(true);  

        Button exitButton = new Button("Exit quiz");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e -> handleBack(primaryStage));
    
        questionMatrices = new HBox(10, matrix1GridPane, matrix2GridPane);
        questionMatrices.getStyleClass().add("matrix-grid");
    
        HBox buttons = new HBox(10, submitButton, nextButton);
        buttons.getStyleClass().add("button-box");
    
        layout.getChildren().addAll(questionLabel, questionMatrices, inputMatrix, feedbackLabel, buttons, exitButton);
    
        Scene matrixMultiplicationQuizScreen = new Scene(layout, 400, 400);
        matrixMultiplicationQuizScreen.getStylesheets().add("styles.css");
    
        return matrixMultiplicationQuizScreen;
    }
    
    public static void handleSubmit() {
        double[][] inputValues = new double[size][size];
    
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                try {
                    inputValues[r][c] = Double.parseDouble(inputFields[r][c].getText().trim());
                } catch (NumberFormatException e) {
                    feedbackLabel.setText("Please enter a valid number in each field.");
                    return;
                }
            }
        }
    
        Matrix inputMatrix = new Matrix(inputValues);
        Matrix answer = currentMatrix1.multiply(currentMatrix2);
    
        if (inputMatrix.equals(answer)) {
            questionsCorrect++;
            feedbackLabel.setStyle("-fx-text-fill: #00ff08;");
            feedbackLabel.setText("Correct!");
            nextButton.setDisable(false);
        } else {
            feedbackLabel.setStyle("-fx-text-fill: rgb(255, 0, 0)");
            feedbackLabel.setText("Incorrect! The correct answer is:\n" + answer);
            nextButton.setDisable(false);
        }
    }
    
    

    public static void handleNext(Stage primaryStage) {   
        if (questionNum >= numQuestions) {
            handleEnd();
        } else {
            questionNum++;
            currentMatrix1 = Module.generateMatrix(size, size);
            currentMatrix2 = Module.generateMatrix(size, size);

            matrix1GridPane.getChildren().clear();
            matrix2GridPane.getChildren().clear();

            fillMatrixGridPane(matrix1GridPane, currentMatrix1);
            fillMatrixGridPane(matrix2GridPane, currentMatrix2);

            questionLabel.setText(questionNum + ". What is the product of these two matrices?");
            feedbackLabel.setText("");
            nextButton.setDisable(true);

            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    inputFields[r][c].clear();
                }
            }
        }     
    }

    public static void fillMatrixGridPane(GridPane gridPane, Matrix matrix) {
        gridPane.getChildren().clear();
        double[][] values = matrix.getValues();
    
        // Left bracket 
        Group leftBracket = new Group();
        Line leftVertical = new Line(0, 0, 0, values.length * 50);
        Line leftTopHorizontal = new Line(0, 0, 10, 0);
        Line leftBottomHorizontal = new Line(0, values.length * 50, 10, values.length * 50);
        
        leftVertical.getStyleClass().add("matrix-border");
        leftTopHorizontal.getStyleClass().add("matrix-border");
        leftBottomHorizontal.getStyleClass().add("matrix-border");

        leftBracket.getChildren().addAll(leftVertical, leftTopHorizontal, leftBottomHorizontal);
        gridPane.add(leftBracket, 1, 0, 1, values.length);
    
        for (int row = 0; row < values.length; row++) {
            for (int col = 0; col < values[row].length; col++) {
                Label label = new Label(String.valueOf(values[row][col]));
                label.getStyleClass().add("matrix-cell");
                gridPane.add(label, col + 2, row);
            }
        }
    
        // Right bracket 
        Group rightBracket = new Group();
        Line rightVertical = new Line(0, 0, 0, values.length * 50);
        Line rightTopHorizontal = new Line(0, 0, -10, 0);
        Line rightBottomHorizontal = new Line(0, values.length * 50, -10, values.length * 50);
        
        rightVertical.getStyleClass().add("matrix-border");
        rightTopHorizontal.getStyleClass().add("matrix-border");
        rightBottomHorizontal.getStyleClass().add("matrix-border");
        
        rightBracket.getChildren().addAll(rightVertical, rightTopHorizontal, rightBottomHorizontal);
        gridPane.add(rightBracket, values[0].length + 2, 0, 1, values.length);

        
    }

    private static void handleBack(Stage primaryStage) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Are you sure you want to quit? Your progress will not be saved.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new HomeScreen().start(primaryStage);
        }
    }

    private static void handleEnd() {
        questionLabel.setText("Quiz complete! You scored " + (questionsCorrect / (double) numQuestions) * 100 + "%");
        questionMatrices.setVisible(false);
        inputMatrix.setVisible(false);
        submitButton.setVisible(false);
        nextButton.setVisible(false);
        feedbackLabel.setText("");
    }
}
