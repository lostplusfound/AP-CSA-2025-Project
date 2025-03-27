import javafx.application.Application;
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
import javafx.scene.text.Text;

public class MultiplicationQuizScreen{
  private static Matrix currentMatrix1;
  private static Matrix currentMatrix2;

  private static Label questionLabel;

  private static Label feedbackLabel;

  private static GridPane matrix1GridPane;
  private static GridPane matrix2GridPane;

  private static GridPane inputGridPane = new GridPane();

  private static TextField[][] inputFields;

  private static HBox questionMatrices;

  private static String difficulty;
  private static int size;
  private static int numQuestions;

  private static int questionsCorrect = 0;

  private static boolean correct = false;

  public static Scene createQuizScene(Stage primaryStage, Module selectedModule){
    difficulty = selectedModule.getDifficulty();
    numQuestions = selectedModule.getNumQuestions();

    if(difficulty.equals("Easy")){
        size = 2;
    } else if(difficulty.equals("Medium")){
        size = 3;
    } else {
        size = 4;
    }

    questionLabel = new Label((questionsCorrect + 1) + ". What is the product of these two matrices?");

    feedbackLabel = new Label("");

    inputFields = new TextField[size][size];

    currentMatrix1 = Module.generateMatrix(size, size);
    currentMatrix2 = Module.generateMatrix(size, size);

    matrix1GridPane = new GridPane();
    matrix2GridPane = new GridPane();

    matrix1GridPane.setVgap(10);
    matrix1GridPane.setHgap(10);
    matrix2GridPane.setVgap(10);
    matrix2GridPane.setHgap(10); 

    fillMatrixGridPane(matrix1GridPane, currentMatrix1);
    fillMatrixGridPane(matrix2GridPane, currentMatrix2);

    for(int r = 0; r < size; r++){
        for(int c = 0; c < size; c++){
            inputFields[r][c] = new TextField();
            inputGridPane.add(inputFields[r][c], c, r);
        }
    }

    Button submitButton = new Button("Submit");
    submitButton.setOnAction(e -> handleSubmit());

    Button nextButton = new Button("Next");
    nextButton.setOnAction(e -> handleNext(primaryStage));

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> handleBack(primaryStage));

    questionMatrices = new HBox(10, matrix1GridPane, matrix2GridPane);

    HBox buttons = new HBox(10, submitButton, nextButton, backButton);

    VBox layout = new VBox(10, questionLabel, feedbackLabel, questionMatrices, inputGridPane, buttons);

    Scene matrixMultiplicationQuizScreen = new Scene(layout, 400, 400);

    return matrixMultiplicationQuizScreen;


  }

  public static void handleSubmit(){
      double[][] inputValues =  new double[size][size];

      for(int r = 0; r < size; r++){
          for(int c = 0; c < size; c++){
              try{
                  inputValues[r][c] = Double.parseDouble(inputFields[r][c].getText().trim());
              } catch(NumberFormatException e){
                  feedbackLabel.setText("Please enter a valid number in each field.");
                  return;
              }
          }
      }

      Matrix inputMatrix = new Matrix(inputValues);

      if(inputMatrix.equals(currentMatrix1.multiply(currentMatrix2))){
          correct = true;
          questionsCorrect++;
          feedbackLabel.setText("Correct!");
      } else{
          correct = false;
          feedbackLabel.setText("Try again!");
      }
  }

  public static void handleNext(Stage primaryStage){
      if(correct){
          if(questionsCorrect == numQuestions){
              handleEnd(primaryStage);
          } else{
              currentMatrix1 = Module.generateMatrix(size, size);
              currentMatrix2 = Module.generateMatrix(size, size);

              matrix1GridPane.getChildren().clear();
              matrix2GridPane.getChildren().clear();

              fillMatrixGridPane(matrix1GridPane, currentMatrix1);
              fillMatrixGridPane(matrix2GridPane, currentMatrix2);

              questionLabel.setText((questionsCorrect + 1) + ". What is the product of these two matrices?");

              feedbackLabel.setText("");

              for(int r = 0; r < size; r++){
                  for(int c = 0; c < size; c++){
                      inputFields[r][c].clear();
                  }
              }
          }
      }
  }

  public static void fillMatrixGridPane(GridPane gridPane, Matrix matrix){
      double[][] values = matrix.getValues();

      Rectangle leftBorder = new Rectangle(1, values.length * 50);
      leftBorder.getStyleClass().add("matrix-border");
      gridPane.add(leftBorder, 0, 0, 1, values.length);

      Rectangle rightBorder = new Rectangle(1, values.length * 50);
      rightBorder.getStyleClass().add("matrix-border");
      gridPane.add(rightBorder, values[0].length + 1, 0, 1, values.length);

      for(int r = 0; r < values.length; r++){
          for(int c = 0; c < values[0].length; c++){
              Label label = new Label(String.valueOf(values[r][c]));
              gridPane.add(label, c + 1, r);
          }
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

  private static void handleEnd(Stage primaryStage) {
          new HomeScreen().start(primaryStage);
  }

}
