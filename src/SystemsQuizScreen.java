import java.util.Optional;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class SystemsQuizScreen {
    private static int questionIndex = 1;
    private static int numCorrect = 0;
    private static Matrix currentMatrix;
    private static GridPane matrixGrid;
    private static int size; 
    private static Label questionLabel;
    private static Label feedbackLabel;
    private static Button noSolutionButton;
    private static Button submitButton;
    private static Button nextButton;
    private static Button exitButton;
    private static TextField[] inputs;

    public static Scene getScene(Stage primaryStage, Module module) {
        questionLabel = new Label(questionIndex + ". What is the solution to this system?");
        questionLabel.getStyleClass().add("question-label");
        size = switch (module.getDifficulty()) {
            case "Easy" -> 2;
            case "Medium" -> 3;
            case "Hard" -> 4;
            default -> throw new IllegalArgumentException("Invalid difficulty");
        };

        // Create matrix grid
        matrixGrid = new GridPane();
        matrixGrid.getStyleClass().add("matrix-grid");
        currentMatrix = Module.generateMatrix(size, size + 1);
        updateQuestion(currentMatrix);




        HBox inputRow = new HBox(10);
        inputRow.getStyleClass().add("input-row");
        inputs = new TextField[size];
        for(int i = 0; i < size; i++) {
            inputs[i] = new TextField();
            inputs[i].getStyleClass().add("answer-box");
            inputs[i].setMaxWidth(120);
            inputs[i].setPromptText("Variable " + (i + 1));
        }
        inputRow.getChildren().addAll(inputs);

        feedbackLabel = new Label();
        feedbackLabel.getStyleClass().addAll("feedback-label");

        noSolutionButton = new Button("No solution");
        noSolutionButton.getStyleClass().add("no-solution-button");

        submitButton = new Button("Submit Answer");
        submitButton.getStyleClass().add("submit-button");

        nextButton = new Button("Next");
        nextButton.getStyleClass().add("next-button");
        nextButton.setDisable(true);

        noSolutionButton.setOnAction(e -> {
            double[] correctAnswer = currentMatrix.solve();
            if (correctAnswer == null) { 
                feedbackLabel.setStyle("-fx-text-fill: #00ff08;");
                feedbackLabel.setText("Correct!");
                submitButton.setDisable(true);
                nextButton.setDisable(false);
                numCorrect++;

            } else {
                String answerString = "";
                for(double d : correctAnswer) {
                    answerString += String.format("%.3f, ", d);
                }
                feedbackLabel.setStyle("-fx-text-fill:rgb(255, 0, 0);");
                feedbackLabel.setText("Incorrect. The correct answer is: " + answerString.substring(0, answerString.length() - 2));
                submitButton.setDisable(true);
                nextButton.setDisable(false);
            }

        });

        submitButton.setOnAction(e -> {
            try {
                double[] userAnswer = new double[size];
                for(int i = 0; i < userAnswer.length; i++) {
                    userAnswer[i] = parseUserAnswer(inputs[i].getText().trim());
                }

                double[] correctAnswer = currentMatrix.solve();

                if (checkAnswer(correctAnswer, userAnswer)) { // Allow small floating-point errors
                    feedbackLabel.setStyle("-fx-text-fill: #00ff08;");
                    feedbackLabel.setText("Correct!");
                    submitButton.setDisable(true);
                    nextButton.setDisable(false);
                    numCorrect++;

                } else {
                    String answerString = "";
                    for(double d : correctAnswer) {
                        answerString += String.format("%.3f, ", d);
                    }
                    feedbackLabel.setStyle("-fx-text-fill:rgb(255, 0, 0);");
                    feedbackLabel.setText("Incorrect. The correct answer is: " + answerString.substring(0, answerString.length() - 2));
                    submitButton.setDisable(true);
                    nextButton.setDisable(false);
                }
            } catch (NumberFormatException ex) {
                feedbackLabel.setStyle("-fx-text-fill: white");
                feedbackLabel.setText("Invalid input. Please enter a number.");
            }
        });

        nextButton.setOnAction(e -> {
            submitButton.setDisable(false);
            nextButton.setDisable(true);
            questionIndex++;

            if (questionIndex <= module.getNumQuestions()) {
                currentMatrix = Module.generateMatrix(size, size + 1);
                updateQuestion(currentMatrix);
                feedbackLabel.setText("");
                for(TextField tf : inputs) {
                    tf.clear();
                }
            } else {
                questionLabel.setText("Quiz complete! You scored "
                        + String.format("%.3f", (numCorrect / (double) module.getNumQuestions()) * 100) + "%");
                matrixGrid.setVisible(false);
                inputRow.setVisible(false);
                submitButton.setVisible(false);
                nextButton.setVisible(false);
                noSolutionButton.setVisible(false);
                feedbackLabel.setText("");
                questionIndex = 1;
                numCorrect = 0;
            }
        });

        exitButton = new Button("Exit quiz");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Are you sure you want to quit? Your progress will not be saved.");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                new HomeScreen().start(primaryStage);
            }
        });
        

        HBox buttonBox = new HBox(10, noSolutionButton, submitButton, nextButton);
        buttonBox.setStyle("-fx-alignment: center;");
        VBox layout = new VBox(20, questionLabel, matrixGrid, inputRow, feedbackLabel, buttonBox, exitButton);
        layout.getStyleClass().add("quiz-layout");

        Scene systemsQuizScene = new Scene(layout, 400, 400);
        systemsQuizScene.getStylesheets().add("styles.css");

        return systemsQuizScene;
    }

    private static void updateQuestion(Matrix matrix) {
        questionLabel.setText(questionIndex + ". What is the solution to this system?");
        matrixGrid.getChildren().clear();
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
        matrixGrid.add(leftBracket, 1, 0, 1, values.length);

        // Adding matrix content (coefficients)
        for (int row = 0; row < matrix.numRows(); row++) {
            for (int col = 0; col < matrix.numCols(); col++) {
                Label label = new Label(String.valueOf(matrix.getElement(row, col)));
                label.getStyleClass().add("matrix-cell");
                matrixGrid.add(label, col + 1, row);
            }
        }

        // Dashed line
        Line dashedLine = new Line();
        dashedLine.setStartY(0);
        dashedLine.setEndY(matrix.numRows() * 50); 
        dashedLine.setStrokeWidth(1); 
        dashedLine.setStroke(javafx.scene.paint.Color.valueOf("#00ff08")); 
        dashedLine.getStrokeDashArray().addAll(10d, 5d); 
        matrixGrid.add(dashedLine, matrix.numCols(), 0, 1, matrix.numRows());

        // Right bracket 
        Group rightBracket = new Group();
        Line rightVertical = new Line(0, 0, 0, values.length * 50);
        Line rightTopHorizontal = new Line(0, 0, -10, 0);
        Line rightBottomHorizontal = new Line(0, values.length * 50, -10, values.length * 50);
        
        rightVertical.getStyleClass().add("matrix-border");
        rightTopHorizontal.getStyleClass().add("matrix-border");
        rightBottomHorizontal.getStyleClass().add("matrix-border");
        
        rightBracket.getChildren().addAll(rightVertical, rightTopHorizontal, rightBottomHorizontal);
        matrixGrid.add(rightBracket, values[0].length + 2, 0, 1, values.length);
    }

    private static double parseUserAnswer(String input) {
        if(input.indexOf('/') >= 0) {
            String[] fraction = input.split("/");
            return Double.parseDouble(fraction[0])/Double.parseDouble(fraction[1]);
        }
        return Double.parseDouble(input);
    }

    private static boolean checkAnswer(double[] correctAnswer, double[] userAnswer) {
        for(int i = 0; i < correctAnswer.length; i++) {
            if(Math.abs(userAnswer[i] - correctAnswer[i]) > 0.0001) return false;
        }
        return true;
    }
}
