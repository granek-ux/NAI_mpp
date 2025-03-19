import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Double> wagi = new ArrayList<Double>();
        double prog;

        System.out.println("podaj dwie wagi");
        for(int i =0; i<2 ;i++)
            wagi.add(sc.nextDouble());
        System.out.println("podaj prog");
        prog = sc.nextDouble();


        Perceptron perceptron = new Perceptron(wagi, prog);

        List<Double> syganly = new ArrayList<Double>();

        System.out.println("podaj dwa sygały");
        for (int i =0; i<2 ;i++)
            syganly.add(sc.nextDouble());

        int compute = perceptron.Compute(syganly);

        System.out.println("Wynik to: " + compute);


    }
}
