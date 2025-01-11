
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Timer extends JPanel {

	private static final long serialVersionUID = 1L;

	private int time = 200; // time to guess in seconds
	private JLabel timeLeftLabel;
	private boolean gameOver; // to end the timer
	private HangmanGUI hangman;

	// constructor
	public Timer(HangmanGUI hangman) {
		this.timeLeftLabel = new JLabel("TIME: " + this.time);
		this.hangman = hangman;
		this.add(timeLeftLabel);
		setVisible(true);
		this.startTimer();
	}

	// threaded method to run the timer
	private void startTimer() {
		Thread timerThread = new Thread() {
			public void run() {
				while (time > 0 && !gameOver) {
					timeLeftLabel.setText("TIME: " + time); // update label
					try {
						Thread.sleep(1000); // wait 1 second
					} catch (Exception e) {
						System.out.println("Error in sleep");
					}
					time--; // decrement timer
				}
				if (time == 0) { // if the time is up
					timeLeftLabel.setText("TIME: " + time); // update timer to 0 seconds
					gameOver = true; // end the game
					hangman.gameEnd("TIME UP! YOU LOSE!");
				}
			}
		};
		timerThread.start(); // start the thread
	}

	// getters and setters:
	
	public int getTime() {
		return time;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	// reset the timer should the game restart
	public void reset() {
		this.time = 200;
		this.gameOver = false;
		startTimer();
	}
}
