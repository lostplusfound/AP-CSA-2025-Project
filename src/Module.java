public class Module {
    private int numQuestions;
    private Difficulty difficulty; 

    public enum Difficulty {
        EASY, MEDIUM, HARD
    };

    public Moddule(int numQuestions, Difficulty difficulty){
        this.numQuestions = numQuestions; 
        this.difficulty = difficulty; 
    }

}