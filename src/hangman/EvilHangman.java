package hangman;

import java.io.File;
import java.io.IOException;

public class EvilHangman {
	
	  public static void main(String[] args) throws IOException, EmptyDictionaryException {		  
	    	EvilHangmanGame hangman = new EvilHangmanGame();	
	    	String filepath = args[0];
	    	int maxLength = Integer.parseInt(args[1]);
	    	int maxGuess = Integer.parseInt(args[2]);	
	    	hangman.setMaxGuess(maxGuess);

	    	hangman.startGame(new File(filepath), maxLength);
	    	hangman.playGame();
	    	hangman.endGame();

	 }
}
