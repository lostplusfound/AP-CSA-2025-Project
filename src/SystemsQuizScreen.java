import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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
            inputs[i].setPromptText("Value of variable " + (i + 1));
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
        exitButton.setOnAction(e -> new HomeScreen().start(primaryStage));

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

        Rectangle leftBorder = new Rectangle(1, matrix.numRows() * 50);
        leftBorder.getStyleClass().add("matrix-border");
        matrixGrid.add(leftBorder, 0, 0, 1, matrix.numRows());

        for (int row = 0; row < matrix.numRows(); row++) {
            for (int col = 0; col < matrix.numCols(); col++) {
                Label label = new Label(String.valueOf(matrix.getElement(row, col)));
                label.getStyleClass().add("matrix-cell");
                matrixGrid.add(label, col + 1, row);
            }
        }

        Rectangle rightBorder = new Rectangle(1, matrix.numRows() * 50);
        rightBorder.getStyleClass().add("matrix-border");
        matrixGrid.add(rightBorder, matrix.numCols() + 1, 0, 1, matrix.numRows());
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
