public class Module {
    private final String quizType;
    private final int numQuestions;
    private final String difficulty;

    public Module(String quizType, int numQuestions, String difficulty) {
        this.quizType = quizType;
        this.numQuestions = numQuestions;
        this.difficulty = difficulty;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuizType() {
        return quizType;
    }
    
    public static Matrix generateMatrix(int length, int width) {
        double[][] vals = new double[length][width];
        for (int r = 0; r < length; r++) {
            for (int c = 0; c < width; c++) {
                vals[r][c] = (int) (Math.random() * 10);
            }
        }
        return new Matrix(vals);
    }
}
