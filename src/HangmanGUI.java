import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class HangmanGUI {
	public JFrame frm = new JFrame("Hangman"); // main window
	public String[] guessed = new String[26]; // array to store the letters that have been guessed
	public int lives = 7; // number of wrong guesses allowed
	public String targetWord; // word to guess
	public JLabel[] slots; // shows the word split up into individual letters
	public JPanel slotsRow; 
	public Gallows gallows = new Gallows(7 - lives); // gallows drawing
	public Timer timer = new Timer(this);
	public JButton submit; // submit button
	public JLabel guessesRemaining; // win/loss message/guesses remaining number
	public JButton chooseGuess; // choose guess button
	public JButton playAgain; // play again button
	public JButton revealWord; // reveal word button

	// constructor
	HangmanGUI(String wordToGuess) {
		this.targetWord = wordToGuess;
		
		frm.setSize(340, 660);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		FlowLayout flowLayout = new FlowLayout(); // set flow layout
		BorderLayout borderLayout = new BorderLayout(); // set border layout
		
		Border border = BorderFactory.createEmptyBorder(15, 35, 10, 35); // padding
		frm.setLayout(borderLayout);

		// elements:
		chooseGuess = new JButton("Guess a letter...");
		JLabel guessTextLabel = new JLabel("Guess: ");
		submit = new JButton("Submit Guess");
		guessesRemaining = new JLabel("Guesses Remaining: " + lives);
		JLabel incorrectGuesses = new JLabel("incorrect guesses: ");
		playAgain = new JButton("Play again?");
		revealWord = new JButton("Reveal actual word...");
		JLabel targetWordReveal = new JLabel(wordToGuess);
		
		// set the elements to invisible that aren't in use until game ended:
		playAgain.setVisible(false); 
		revealWord.setVisible(false);
		targetWordReveal.setVisible(false);

		slots = new JLabel[targetWord.length()]; // array of letter slots
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new JLabel("_"); // represent the empty slot with an underscore
		}
		
		GridLayout gridLayout = new GridLayout(1, slots.length, 10, 0); // set grid layout

		// code to execute upon clicking the guess letter button
		chooseGuess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayChoice(guessTextLabel, guessed);
			}
		});

		// code to execute upon clicking submit button
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// update the guessed letters array
				guessed = processGuess(guessTextLabel, guessed, targetWord, guessesRemaining, incorrectGuesses);
				if (guessed[0] != null) { // if the guessed letter exists:
					if (guessed[0].length() > 1) { // if the guessed letter is a win/loss message
						gameEnd(guessed[0]);
					}
				}
			}
		});
		
		// code to execute on pressing the play again button
		playAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// reset variables:
				guessed = new String[26];
				lives = 7;
				targetWord = Main.getWord();
				slots = new JLabel[targetWord.length()];
				for (int i = 0; i < slots.length; i++) {
					slots[i] = new JLabel("_");
				}
				guessTextLabel.setText("Guess: ");
				guessesRemaining.setText("Guesses Remaining: " + lives);
				incorrectGuesses.setText("incorrect guesses: ");
				submit.setEnabled(true);
				chooseGuess.setEnabled(true);
				playAgain.setVisible(false);
				revealWord.setVisible(false);
				targetWordReveal.setVisible(false);
				targetWordReveal.setText(targetWord);
				revealWord.setText("Reveal actual word...");
				slotsRow.removeAll();
				for (JLabel j : slots) {
					slotsRow.add(j);
				}
				gallows.reset(); // reset gallows drawing
				timer.reset(); // start timer again
			}
		});
		
		// code to execute upon clicking the reveal word button
		revealWord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (revealWord.getText().equals("Reveal actual word...")) {
					targetWordReveal.setVisible(true);
					revealWord.setText("Hide word");
				} else {
					targetWordReveal.setVisible(false);
					revealWord.setText("Reveal actual word...");
				}
			}
		});

		// add elements to the frame
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(border); // add padding
		
		// timer:
		JPanel timerRow = new JPanel(flowLayout);
		timerRow.add(timer);
		
		// top row:
		JPanel topRow = new JPanel(flowLayout);
		topRow.add(chooseGuess);
		topRow.add(guessTextLabel);
		topRow.add(submit);
		
		// slots row:
		slotsRow = new JPanel(gridLayout);
		for (JLabel j : slots) {
			slotsRow.add(j);
		}
		
		// guesses row:
		JPanel guessesRow = new JPanel(flowLayout);
		guessesRow.add(guessesRemaining);
		
		// bottom row:
		JPanel bottomRow = new JPanel(flowLayout);
		bottomRow.add(incorrectGuesses);
		bottomRow.add(playAgain);
		
		// gallows drawing row:
		JPanel gallowsRow = new JPanel(flowLayout);
		gallows.setPreferredSize(new Dimension(300, 350));
		gallowsRow.add(gallows);
		
		// end game elements drawing row:
		JPanel endGameRow = new JPanel(flowLayout);
		endGameRow.add(revealWord);
		endGameRow.add(targetWordReveal);
		
		// add all panels:
		mainPanel.add(timerRow, BorderLayout.NORTH);
		mainPanel.add(topRow, BorderLayout.NORTH);
		mainPanel.add(slotsRow, BorderLayout.CENTER);
		mainPanel.add(guessesRow, BorderLayout.SOUTH);
		mainPanel.add(bottomRow, BorderLayout.SOUTH);
		mainPanel.add(gallowsRow, BorderLayout.SOUTH);
		mainPanel.add(endGameRow, BorderLayout.SOUTH);
		
		frm.add(mainPanel);
		
		frm.setVisible(true); // show the frame
	}
	
	// method to end the game
	public void gameEnd(String message) {
		submit.setEnabled(false); // disable the submit button
		guessesRemaining.setText(message); // show the win/loss message
		chooseGuess.setEnabled(false); // disable the choose guess button
		playAgain.setVisible(true); // show the play again button
		if (message.contains("YOU LOSE!")) {
			revealWord.setVisible(true); // show the reveal word button
		}
		timer.setGameOver(true); // stop timer
	}

	// method to process a selected letter for a guess:
	public String[] processGuess(JLabel guessTextLabel, String[] guessed, String wordToGuess,
			JLabel guessesRemaining, JLabel incorrectGuesses) {
		String rawText = guessTextLabel.getText();
		String guess = rawText.substring(rawText.indexOf(":") + 2); // get the letter guessed
		// return the current array if the guess has already been made, or isn't a valid character:
		if (incorrectGuesses.getText().contains(guess) || guess.length() < 1 || guess == null) {
			return guessed;
		}
		// insert the guessed letter at the first available position in the array:
		for (int i = 0; i < guessed.length; i++) {
			if (guessed[i] == null) {
				guessed[i] = guess;
				break;
			}
		}
		
		String[] wordSplit = wordToGuess.toUpperCase().split(""); // split the target into characters
		boolean found = false;
		
		// see if the target word contains the guessed letter:
		for (int i = 0; i < wordSplit.length; i++) {
			if (guess.equals(wordSplit[i])) {
				slots[i].setText(guess);
				found = true;
			}
		}
		
		if (!found) { // the guess is incorrect
			lives--; // decrement lives
			guessesRemaining.setText("Guesses remaining: " + lives);
			incorrectGuesses.setText(incorrectGuesses.getText() + guess + ", ");
			gallows.drawNextGallowPiece(lives); // update the gallows drawing
			frm.repaint(); // redraw the frame - show the new gallows
		}
		
		// calculate the number of letters yet to be correctly guessed:
		int empties = 0;
		for (JLabel j : slots) {
			if (j.getText().equals("_")) {
				empties++;
			}
		}
		
		if (empties == 0 && lives > 0) { // if there are no letters left to guess and lives above 0
			String[] win = { "YOU WIN!" };
			return win;
		} else if (lives == 0) { // if there are no lives left
			String[] lose = { "YOU LOSE!" };
			return lose;
		}
		guessTextLabel.setText("Guess: ");
		return guessed;
	}

	// method to provide the player with letter choices
	private void displayChoice(JLabel guessTextLabel, String[] guessed) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // all valid choices
		StringBuilder deleter = new StringBuilder(alphabet);
		
		// loop over the guessed letters and delete them from the alphabet:
		for (String l : guessed) {
			if (l != null) {
				int i = deleter.indexOf(l);
				if (i != -1) {
					deleter.deleteCharAt(i);
				}
			}
		}
		alphabet = deleter.toString(); // processed alphabet
		String[] letterSelection = alphabet.split("");
		
		// show the selection window for each letter in the alphabet
		
		String selectedLetter = (String) JOptionPane.showInputDialog(frm, "Guess:", "Guess",
				JOptionPane.QUESTION_MESSAGE, null, letterSelection, letterSelection[0]);
		if (selectedLetter != null) { // if the user has selected a letter
			guessTextLabel.setText("Guess: " + selectedLetter); // update the guess
		}
	}
}
