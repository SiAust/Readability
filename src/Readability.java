import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Readability {
    private static String text;
    private static String[] sentences;
    private static String[] words;
    private static int wordCount;
    private static int charCount;
    private static int syllables;
    private static int polysyllables;
    private static double score;
    private static List<Integer> ages = new ArrayList<>();

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        df.setRoundingMode(RoundingMode.FLOOR);

        Scanner sc = new Scanner(System.in);

        String path;
        File file = null;
        if (args.length > 0) {
            path = args[0];
            file = new File("./" + path);
        } else {
            file = new File("empty argument");
        }

        // Try to open file using arg path. Split text into sentences.
        try (Scanner fileScan = new Scanner(file)) {
            text = fileScan.nextLine();
            System.out.println(String.format("The text is:\n%s", text + "\n"));
            String pattern = "[!?.]\\s?"; // match one of [range] characters, zero or one whitespace character
            sentences = text.split(pattern); // split input into sentences

            wordCount = 0;

            words = text.split("\\s?[!?.,]?\\s+");
            wordCount = words.length;

            // Find the character count
            charCount = 0;
            Pattern pattern1 = Pattern.compile("[\\w\\S]");
            Matcher matcher = pattern1.matcher(text);
            while (matcher.find()) {
                charCount++;
            }

            syllables();

            System.out.println(String.format("Words: %d", wordCount)); // words
            System.out.println(String.format("Sentences: %d", sentences.length)); // sentences
            System.out.println(String.format("Characters: %d", charCount)); // characters
            System.out.println(String.format("Syllables: %d", syllables)); // syllables
            System.out.println(String.format("Polysyllables: %d", polysyllables)); // polysyllables

            // Ask the score method
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            String input = sc.nextLine();

            System.out.println(); // blank line

            switch (input) {
                case "ARI":
                    ari();
                    break;
                case "FK":
                    fk();
                    break;
                case "SMOG":
                    smog();
                    break;
                case "CL":
                    cl();
                    break;
                case "all":
                    all();
                    break;
                default:
                    System.out.println("Unknown command. Exiting.");
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not found. Error: " + e);
        }


    }

    private static void ari() {
        // Formula to update score
        score = 4.71d * ((double) charCount / wordCount) + 0.5d * ((double) wordCount / sentences.length) - 21.43d;
        ages.add(getRange(score));
        System.out.println(String.format(String.format("Automated Readability Index: %s (about %d year olds).", df.format(score), getRange(score))));
    }

    /* Flesch-Kincaid readability test */
    private static void fk() {
        // Formula to update score
        score = 0.39d * ((double) words.length / sentences.length) + 11.8d * ((double) syllables / words.length) - 15.59d;
        ages.add(getRange(score));
        System.out.println(String.format("Flesch–Kincaid readability tests: %s (about %d year olds).", df.format(score), getRange(score)));
    }

     /* Simple Measure of Gobbledygook index */
    private static void smog() {
        score = 1.043d * Math.sqrt(polysyllables * ((double) 30 / sentences.length)) + 3.1291d;
        ages.add(getRange(score));
        System.out.println(String.format("Simple Measure of Gobbledygook: %s (about %d year olds).", df.format(score), getRange(score)));
    }

    /* Coleman-Liau index */
    private static void cl() {
        String[] words = text.split("\\s?[!?.,]?\\s+");
        String[] characters = text.split("\\s");
        int count = 0;
        for (String character : characters) {
            count += character.length();
        }

        double L = (double) count / words.length * 100;
        double S = (double) sentences.length / words.length * 100;

        score = 0.0588 * L - 0.296 * S - 15.8;
        ages.add(getRange(score));
        System.out.println(String.format("Coleman–Liau index: %s (about %d year olds).", df.format(score), getRange(score)));

    }

    /* Print all scores for all index tests */
    private static void all() {
        ari();
        fk();
        smog();
        cl();
        double sum = 0;
        for (int age : ages) {
            sum += age;
        }
        double averageAge = sum / ages.size();
        System.out.println(String.format("\nThis text should be understood by %s year olds.", df.format(averageAge)));
    }

    /* update the syllables field */
    private static void syllables() {
        Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u', 'y', 'E'); // todo: add further capital vowels?
        int cumulative = 0;
        polysyllables = 0;
        for (String word : words) {
            syllables = 0;
            if (vowels.contains(word.charAt(0))) {
                syllables++;
            }
            for (int i = 1; i < word.length(); i++) {
                if (vowels.contains(word.charAt(i)) ) {
                    if (vowels.contains(word.charAt(i - 1))) { // capital 'E' not matched with 'e'; fixed. Other capital vowels?
                        if (i == 1) {
                            syllables--;
                        }
                        continue;
                    } else if (i == word.length() -1 && word.charAt(i) == 'e') {
                        continue;
                    }
                    syllables++;
                }
            }
            if (syllables == 0) {
                syllables = 1;
            }
            if (syllables > 2) {
//                System.out.println("Polysyllable word= " + word + " Syllables= " + syllables);
                polysyllables++;
            }
//            System.out.println("word= " + word + " Syllables= " + syllables);
            cumulative += syllables;
        }
        syllables = cumulative;
//        System.out.println("polysyllables= " + polysyllables);
//        System.out.println("cumulative= " + cumulative);
    }

    private static int getRange(double score) {
        switch ((int) Math.floor(score)) {
            case 1:
                return  6;
            case 2:
                return  7;
            case 3:
                return  9;
            case 4:
                return  10;
            case 5:
                return  11;
            case 6:
                return  12;
            case 7:
                return  13;
            case 8:
                return  14;
            case 9:
                return  15;
            case 10:
                return  16;
            case 11:
                return  17;
            case 12:
                return  18;
            case 13:
            case 14:
                return  24;
            default:
                return  99;
        }
    }
}
