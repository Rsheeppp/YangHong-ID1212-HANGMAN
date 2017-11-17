package Hangman;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import HServer.Controller;
import java.util.Random;

public class Hangman {

    private Controller connectionToClient;
    private String word;
    private int count;
    private char[] wordArray;
    private char[] maskWord;
    //Constructor
    public Hangman(Controller connectionToClient) throws IOException {
        this.connectionToClient = connectionToClient;
        this.word = getWord();
        this.count = 0;
    }
    

	public int start() throws IOException {
        System.out.println("Client" + Thread.currentThread().getName() + " is currently guessing " + word);
        wordArray = word.toCharArray();
        maskWord = new char[wordArray.length];
        count = wordArray.length;
        // Loop for filing up an a array with dashes ( mask the word )
        for (int i = 0; i < maskWord.length; i++) {
            maskWord[i] = '_';
        }
        while (true) {
        	//String printMasked = maskWord.toString(); switch the maskWord to string won't work
            connectionToClient.printToClient(maskWord);
            connectionToClient.printToClient("Input A letter");
            String clientGuess = connectionToClient.readClientInput();
            //return 0,1 and 2 which mean "client lost connect", "win" and "lose"
            if (clientGuess.equals(null)) {
                return 0;
            }
            //return 1(win) when the client type the whole word and it's correct
            if (word.equals(clientGuess) || word.equals(new String(maskWord))) {
                connectionToClient.printToClient("Congratulation! you guessed it!");
                return 1;
            }
            // Check the letter client input, if it's in the word then update the mask else count down the chance
            if (!checkLetterGuessed(clientGuess)) {
                connectionToClient.printToClient("There is no " + clientGuess + " found!");
                count--;
                connectionToClient.printToClient("You have " + count + " guesses left !");
            }
            //return 1(win) when the whole word is guessed
            if (word.equals(maskWord)) {
                connectionToClient.printToClient("Congratulation! you guessed it!");
                return 1;
            }
            // when the count reaches 0 return the message the game is over
            if (count == 0) {
                connectionToClient.printToClient("Game over!You are HANGED! The word was: " + word);
                return 2;
            }
        }
    }
    private String getWord() throws IOException {
        // Input the words.txt to get words
        try (
        		InputStream in = Reader.class.getResourceAsStream("/words.txt");
        		//FileInputStream getFile = new FileInputStream("/src/Hangman/words.txt");
             BufferedReader getData = new BufferedReader(new InputStreamReader(new DataInputStream(in)))) {
            String w = null ;
            //Get a random word
            Random ran = new Random();
            int random = ran.nextInt(50000)%(50000-0+1) + 0;
            for (int i = 0; i < random; i++) {
                w = getData.readLine();
            }
            return w;
        } catch (IOException ex) {
            throw ex;
        }
    }
    public boolean checkLetterGuessed(String clientGuess) {
        boolean found = false;
        //Check if the input is in the word
        for (int i = 0; i < wordArray.length; i++) {
            // If the input is in the word, replace a dash with the input
            if (clientGuess.charAt(0) == wordArray[i]) {
                maskWord[i] = clientGuess.charAt(0);
                found = true;
            }
        }
        return found;
    }
}