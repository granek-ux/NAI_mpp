import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj k: ");
        int k = scanner.nextInt();

        Pliki pliki = new Pliki(k);

        int wektorIlosc = pliki.getVectorSize();

        while (true)
        {
            List<Double> vector = new ArrayList<>();
            System.out.print("Podaj dane do vecotra: ");
            for(int i = 0; i < wektorIlosc; i++)
                vector.add(scanner.nextDouble());



            System.out.print("Kwiat jest:  ");
            System.out.println(pliki.DlaJednegoWektora(new Obserwacja(vector)));
        }
    }
}
