import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Readability {

    private static final String HARD = "HARD";
    private static final String EASY = "EASY";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();

        String pattern = "[!?.]\\s?"; // match one of [range] characters, zero or one whitespace character

        String[] sentences = text.split(pattern); // split input into sentences

        int wordCount = 0;
        for (int i = 0; i < sentences.length; i++) {
            String[] words = sentences[i].split("\\s+,?"); // split sentences into words
            wordCount += words.length; // count & store number of words
        }
        System.out.println(wordCount / sentences.length <= 10 ? EASY : HARD);
    }
}
