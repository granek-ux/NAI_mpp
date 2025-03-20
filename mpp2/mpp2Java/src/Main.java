import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final String testFileName = "../iris_test.txt";
    static final String trainingFileName = "../iris_training.txt";

    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron(testFileName, trainingFileName);
        int size_limit = perceptron.getSizeTrainingData();
        Scanner sc = new Scanner(System.in);

//        List<Double> wagi = new ArrayList<Double>();
//
//        System.out.println("podaj " + size_limit + " wag:");
//        for(int i =0; i<size_limit ;i++)
//            wagi.add(getDoubleFromScanner(sc));
//
//
//        System.out.println("podaj próg");
//        double prog = getDoubleFromScanner(sc);

//        perceptron.Start(wagi, prog);

        while (true) {
            List<Double> sygnaly  = new ArrayList<>();

            System.out.println("podaj dane wejsciowe: ");
            for (int i = 0; i < size_limit; i++)
                sygnaly .add(getDoubleFromScanner(sc));

            String wynik = perceptron.Compute(sygnaly ) == 1 ? "Iris-setosa" : "To nie setosa";
            System.out.println("Wynik to: " + wynik);
        }
    }


    private static double getDoubleFromScanner(Scanner sc) {
       return Double.parseDouble(sc.next().replaceAll(",",".").trim());
    }
}
