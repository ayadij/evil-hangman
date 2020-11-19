package hangman;

import java.io.*;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{

	private List<String> dict;
	private int length;
	private int maxGuess;
	private int guessesLeft;	
	private SortedSet<String> candidates = new TreeSet<>() ;
	private SortedSet<Character> guesses;
	private String pattern;
	private int countDiff;	
	
	@Override
	public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
			//initialize from the input
			Scanner fileSc = new Scanner(dictionary);	
			dict = new ArrayList<String>();
			while (fileSc.hasNext())
				dict.add(fileSc.next().toLowerCase());
			if (dict.size()==0) {
				fileSc.close();
				throw new EmptyDictionaryException("Empty dictionary file");
			}
			if(wordLength<=0)
				throw new EmptyDictionaryException("word length is 0");
			this.length = wordLength;
			this.guessesLeft = maxGuess;
			this.candidates = this.addDict(); //initialize with dictionary words
			this.guesses = new TreeSet<Character>(); //already guessed letters
			this.pattern = ""; //initialize pattern
			for (int i=0; i<this.length; i++){
				this.pattern += "-";
			}
			fileSc.close();
	}
	
	public void playGame() {
		Scanner console = new Scanner(System.in);
		//loop of guesses
		while (guessesLeft > 0 && pattern.contains("-")) {
			System.out.println("You have "+ guessesLeft + " guess left...");
			System.out.println("Used letters: " + getGuessedLetters());
			System.out.println("word: " + pattern);
			System.out.print("Enter guess: ");
			char ch = console.next().toLowerCase().charAt(0);
			try {
				checkGuess(ch);
				makeGuess(ch);
			} catch (InvalidGuessException e) {
				System.out.println(e);
			} catch (GuessAlreadyMadeException e) {
				System.out.println(e);
			}


			if (countDiff == 0) {
				System.out.println("Sorry, there are no " + ch + "'s");
			} else if (countDiff == 1) {
				guessesLeft++;
				System.out.println("Yes, there is one " + ch);
			} else {
				guessesLeft++;
				System.out.println("Yes, there are " + countDiff + " " + ch + "'s");
			}
			
			System.out.println();
		}
		console.close();
	}

	public void checkGuess(char guess) throws InvalidGuessException {
		guess = Character.toLowerCase(guess);
		if (guess < 'a' || guess > 'z') {
			throw new InvalidGuessException("Woops! " + guess + "is an invalid guess. Enter a character a-z");
		}
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		guess = Character. toLowerCase(guess);
		if (guesses.contains(guess)) {
//			guessesLeft--;
			throw new GuessAlreadyMadeException("You already guessed " + guess );
		}

		Map<String, SortedSet<String>> map = new TreeMap<>();
		for (String word : candidates) {
			String p = buildPattern(guess, word);
			if (map.containsKey(p))
				map.get(p).add(word);
			else{
				SortedSet<String> set = new TreeSet<String>();
				set.add(word);
				map.put(p, set);
			}
		}	  
		//System.out.println(map);
		String key = getMaxCandidatesKey(map, guess);
		int diff = 0;
		if (!pattern.equals(key))
			diff = difference(key, pattern);
		guessesLeft--;
		countDiff = diff;
		pattern = key;
		candidates = map.get(key);
		System.out.println(key);
		System.out.println(candidates);
		guesses.add(guess);
		return candidates;
	}
	
	public void setMaxGuess(int maxGuess){
		this.maxGuess = maxGuess;
	}
	
	@Override
	public SortedSet<Character> getGuessedLetters() {		
		return guesses;
	}
	
	//add dictionary words with same length
	public SortedSet<String> addDict() throws EmptyDictionaryException {		
		SortedSet<String> set = new TreeSet<String>();
		for (String word : dict) {
			if (word.length() == length)
				set.add(word);
		}
		if (set.size()==0) {
			throw new EmptyDictionaryException("cannot find that length in dict");
		}
		return set;
	}

	private int difference(String word, String word2) {
		int diff = 0;
		for (int i=0; i<word.length(); i++){
			if (word.charAt(i) != word2.charAt(i))
				diff++;
		}
		return diff;
	}

	private String buildPattern(char guess, String word) {	  
		String p = "";
		for (int i = 0; i<word.length(); i++){
			if ( guess == word.charAt(i))
				p += Character.toString(guess);
			else
				p += pattern.substring(i, i+1);
		}
		return p;
	}
	
	private int countChars(String str, char ch) {
		int count =0;
		for (int i = 0; i < str.length(); i++) {
		    if (str.charAt(i) == ch) {
		        count++;
		    }
		}
		return count;
	}

	private String getMaxCandidatesKey(Map<String, SortedSet<String>> map, char ch){
		int max = 0;
		String res = "";
		boolean replace = false;
		for (String key : map.keySet()){
			if (map.get(key).size() > max){
				replace =true;
			} else if (map.get(key).size() == max) {
				if (countChars(key,'-') > countChars(res,'-'))
					replace = true; 
				else if (countChars(key, ch) < countChars(res,ch))
					replace = true;
				else if (countChars(key, ch) == countChars(res,ch)) {
					if (key.indexOf(ch)>res.indexOf(ch))
						replace = true;
				}
			}
			if (replace) {
				max = map.get(key).size();
				res = key;
				replace = false;
			}
		}	
		return res;
	}

	public void endGame() throws EmptyDictionaryException {
		if (candidates.size()==0)
			throw new EmptyDictionaryException("candidates is empty");
		String answer = candidates.iterator().next();		
		if (guessesLeft > 0) {
			guessesLeft++;
			System.out.println("You win!");
		} else {
			System.out.println("Sorry, you lose!");
		}
		System.out.println("The word was: " + answer);
	}
	
    public static void main(String[] args) throws IOException, EmptyDictionaryException {
    	String DICTIONARY = "dictionary.txt";
    	String SMALL_DICTIONARY = "small.txt";
    	String EMPTY_DICTIONARY = "empty.txt";
    	EvilHangmanGame studentGame = new EvilHangmanGame();
    	
    	studentGame.startGame(new File(EMPTY_DICTIONARY), 4);
	//	studentGame.startGame(new File(DICTIONARY), 1);
	//	studentGame.startGame(new File(SMALL_DICTIONARY), 15);
    	//studentGame.startGame(new File(DICTIONARY), 0);


    }
}
