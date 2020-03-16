import java.util.Scanner;

public class Readability {

    private static final String HARD = "HARD";
    private static final String EASY = "EASY";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        if (text.length() <= 100) {
            System.out.println(EASY);
        } else {
            System.out.println(HARD);
        }
    }
}
