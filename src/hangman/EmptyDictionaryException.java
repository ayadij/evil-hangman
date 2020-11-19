package hangman;

public class EmptyDictionaryException extends Exception {
	static final long serialVersionUID = 1L;
	 public EmptyDictionaryException( String s)  { 
	        super( s); 
	    }
}
