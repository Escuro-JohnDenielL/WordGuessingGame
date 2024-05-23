package COMP009;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordGuessingGame {
    private static final String WORD_LIST_FILE = "dictionary.csv";
    private static final int MAX_GUESSES = 6;

    private String wordToGuess;
    private StringBuilder obscuredWord;
    private List<Character> guessedCharacters;
    private int remainingGuesses;
    private List<String> wordList;

    public WordGuessingGame() {
        guessedCharacters = new ArrayList<>();
        remainingGuesses = MAX_GUESSES;
        loadWordList();
        selectRandomWord();
        initObscuredWord();
    }

    private void loadWordList() {
        wordList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(WORD_LIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading word list file: " + e.getMessage());
            System.exit(1);
        }

        if (wordList.isEmpty()) {
            System.err.println("Word list file is empty.");
            System.exit(1);
        }
    }

    private void selectRandomWord() {
        Random random = new Random();
        int index = random.nextInt(wordList.size());
        wordToGuess = wordList.get(index).toUpperCase();
    }

    private void initObscuredWord() {
        obscuredWord = new StringBuilder();
        for (int i = 0; i < wordToGuess.length(); i++) {
            obscuredWord.append("_");
        }
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (remainingGuesses > 0) {
                System.out.println("Word: " + obscuredWord);
                System.out.print("Guess a letter: ");
                String input = scanner.nextLine().trim().toUpperCase();

                if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                    System.out.println("Invalid input. Please enter a single letter.");
                    continue;
                }

                char letter = input.charAt(0);

                if (guessedCharacters.contains(letter)) {
                    System.out.println("You've already guessed that letter. Try again.");
                    continue;
                }

                guessedCharacters.add(letter);

                if (wordToGuess.indexOf(letter) == -1) {
                    remainingGuesses--;
                    System.out.println("Wrong guess! Remaining guesses: " + remainingGuesses);
                } else {
                    updateObscuredWord(letter);
                }

                if (obscuredWord.toString().equals(wordToGuess)) {
                    System.out.println("Congratulations! You guessed the word: " + wordToGuess);
                    break;
                }
            }

            if (remainingGuesses == 0) {
                System.out.println("Game over! The word was: " + wordToGuess);
            }
        } finally {
            scanner.close();
        }
    }

    private void updateObscuredWord(char letter) {
        StringBuilder newObscuredWord = new StringBuilder();
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter) {
                newObscuredWord.append(letter);
            } else {
                newObscuredWord.append(obscuredWord.charAt(i));
            }
        }
        obscuredWord = newObscuredWord;
    }

    public static void main(String[] args) {
        WordGuessingGame game = new WordGuessingGame();
        game.startGame();
    }
}