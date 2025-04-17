import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String testFileName = "../iris_test.txt";
    private static final String treningFileName = "../iris_training.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier(treningFileName, testFileName);


        int limitSize = naiveBayesClassifier.atributesSize();

        while (true) {
            List<Double> sygnaly = new ArrayList<>();

            for (int i = 0; i < limitSize; i++)
                sygnaly.add(getDoubleFromScanner(sc));

            Obserwacja obs = FileHandling.changeForRange(sygnaly, null);

            System.out.println(naiveBayesClassifier.classify(obs));
        }
    }


    private static double getDoubleFromScanner(Scanner sc) {
        return Double.parseDouble(sc.next().replaceAll(",", ".").trim());
    }
}
