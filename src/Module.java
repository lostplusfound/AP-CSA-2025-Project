public class Module {
    private final String quizType; 
    private final int numQuestions;
    private final Difficulty difficulty;

    // Enum for difficulty levels
    public enum Difficulty {
        EASY, MEDIUM, HARD
    };

    // Constructor to initialize the module with number of questions and difficulty
    public Module(String quizType, int numQuestions, Difficulty difficulty) {
        this.quizType = quizType;
        this.numQuestions = numQuestions;
        this.difficulty = difficulty;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

}
