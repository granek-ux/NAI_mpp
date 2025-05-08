import java.util.Scanner;

public class Main {

    private static final String testFileName = "../iris_test.txt";
    private static final String treningFileName = "../iris_training.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Podaj k:");
        int k = sc.nextInt();
        Grouping g = new Grouping(k, treningFileName);
    }

    private static double getDoubleFromScanner(Scanner sc) {
        return Double.parseDouble(sc.next().replaceAll(",", ".").trim());
    }
}
