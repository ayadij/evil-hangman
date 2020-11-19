package hangman;

public class InvalidGuessException extends Exception {
    static final long serialVersionUID = 1L;
    public InvalidGuessException( String s)  {
        super( s);
    }
}
