import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static int capicity;

    public static void setCapicity(int capicity) {
        Main.capicity = capicity;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Dataset> datasets = FileHandling.ReadDatasetsFromFile("../plecak.txt");
        List<Item> items = new ArrayList<>();
        System.out.println("Podaj id Dataset od 1 do " + datasets.size());
        System.out.println("Jeśli zostanie podane coś innego liczba zostanie wylosowana");
        Random rand = new Random();
        int i;
        try {
            i = sc.nextInt();
            if (i > datasets.size() || i <=0)
                throw new Exception();
        } catch (Exception e) {
            i = rand.nextInt(datasets.size());
            System.out.println("Wylosowano " + i);
        }
        {
            Dataset dataset = datasets.get(i - 1);

            int[] vals = getInt(dataset.vals());
            int[] sizes = getInt(dataset.sizes());
            for (int j = 0; j < vals.length; j++) {
                items.add(new Item(vals[j], sizes[j]));
            }
        }

        Backpack backpack = new Backpack(capicity, items);

        Solve s = new Solve(backpack);

        s.bruteForce();
        System.out.println();
        System.out.println();
        System.out.println();
        s.heurystka();
    }

    private static int[] getInt(String line) {
        int start = line.indexOf('{') + 1;
        int end = line.indexOf('}');
        String numbersOnly = line.substring(start, end).trim();

        String[] strings = numbersOnly.split(",\\s*");

        int[] numbers = new int[strings.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(strings[i]);
        }
        return numbers;
    }
}
