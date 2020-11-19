package hangman;

public class GuessAlreadyMadeException extends Exception {
	static final long serialVersionUID = 1L;
	 public GuessAlreadyMadeException( String s)  { 
	        super( s); 
	    }
}
