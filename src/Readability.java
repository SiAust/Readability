import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Readability {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String path;
        File file = null;
        if (args.length > 0) {
            path = args[0];
            file = new File("./" + path);
        } else {
            file = new File("empty argument");
        }

        try (Scanner fileScan = new Scanner(file)) {
            String text = fileScan.nextLine();
            System.out.println(String.format("The text is:\n%s", text + "\n"));
            String pattern = "[!?.]\\s?"; // match one of [range] characters, zero or one whitespace character
            String[] sentences = text.split(pattern); // split input into sentences
            String[] words = null;

            int wordCount = 0;
            for (int i = 0; i < sentences.length; i++) {
                words = sentences[i].split("\\s+,?"); // split sentences into words
                wordCount += words.length; // count & store number of words
            }

            // Find the character count
            int charCount = 0;
            Pattern pattern1 = Pattern.compile("[\\w\\S]");
            Matcher matcher = pattern1.matcher(text);
            while (matcher.find()) {
                charCount++;
            }

            // Calculate score and match to Automated Readability Index age range for output
            double score = 4.71d * ((double) charCount / wordCount) + 0.5d * ((double) wordCount / sentences.length) - 21.43d;
            String range;

            // Format the double score to have two decimal places and round down
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.FLOOR);
            switch ((int) Math.ceil(score)) {
                case 1:
                    range = "5-6";
                    break;
                case 2:
                    range = "6-7";
                    break;
                case 3:
                    range = "7-9";
                    break;
                case 4:
                    range = "9-10";
                    break;
                case 5:
                    range = "10-11";
                    break;
                case 6:
                    range = "11-12";
                    break;
                case 7:
                    range = "12-13";
                    break;
                case 8:
                    range = "13-14";
                    break;
                case 9:
                    range = "14-15";
                    break;
                case 10:
                    range = "15-16";
                    break;
                case 11:
                    range = "16-17";
                    break;
                case 12:
                    range = "17-18";
                    break;
                case 13:
                    range = "18-24";
                    break;
                case 14:
                    range = "24+";
                    break;
                default:
                    range = "unknown";
            }

            System.out.println(String.format("Words: %d", wordCount));
            System.out.println(String.format("Sentences: %d", sentences.length));
            System.out.println(String.format("Characters: %d", charCount));
            System.out.println(String.format("The score is: %s", df.format(score)));
            System.out.println(String.format("This text should be understood by %s year olds.", range));

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Error: " + e);
        }


    }
}
