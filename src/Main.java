import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Main {
    private static ArrayList<String> targetWords = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("wordlist.txt"));
        while(in.hasNext()){
            targetWords.add(in.next());
        }
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new HangmanGUI(getWord());
			}
		});
    }

    // displays target word in the console:
    public static String getWord(){
        Random r = new Random();
        String word = targetWords.get(r.nextInt(targetWords.size()));
        System.out.println("The target word is: " + word); // output target word
        return word;
    }
}
