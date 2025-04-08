import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static final String testFileName = "../daneTestowe";
    static final String traning = "../daneTreningowe";

    public static void main(String[] args) {

        System.out.println("Rozpoczeto uczenie");

        FileHandling fh = new FileHandling(traning);

        List<Obserwacja> trainingData = fh.getObserwacje();
        Set<String> languages = fh.getLanguages();

        long start = System.nanoTime();

        List<Perceptron> perceptrons = languages.parallelStream()
                .map(language -> new Perceptron(language, trainingData))
                .collect(Collectors.toList());

        double time = (System.nanoTime() - start) / 1_000_000_000F;


        System.out.println("Uczenie trwało: " + time + " sec");

        test(perceptrons);

        SwingUtilities.invokeLater(() -> new RecognitionApp(perceptrons));
    }

    private static void test(List<Perceptron> perceptrons) {
        FileHandling fh = new FileHandling(testFileName);
        List<Obserwacja> testData = fh.getObserwacje();

        int counter = testData.stream()
                .map(obs -> obs.getAtrybutDecyzyjny().equalsIgnoreCase(getResultFromPerceptron(perceptrons, obs.getListaAtrybutowWarunkowych())) ? 1 : 0)
                .mapToInt(Integer::intValue)
                .sum();

        int length = testData.size();

        double percent = (double) counter / (double) length;
        percent*=100;

        System.out.println("ilość poprawnych: " + counter + " z " + length);
        System.out.println("procent to: " + percent + " %");

    }

    private static String getResultFromPerceptron(List<Perceptron> perceptrons, List<Double> inputs) {
//        return perceptrons.stream()
//                .max(Comparator.comparingDouble(p -> p.Compute(inputs)))
//                .map(Perceptron::getLanguage)
//                .orElse("");

        double max = Double.NEGATIVE_INFINITY;
        String maxString = "";

        for (Perceptron perceptron : perceptrons) {
            double y = perceptron.compute(inputs);
            if (y > max) {
                max = y;
                maxString = perceptron.getLanguage();
            }
            System.out.println(y + " " + perceptron.getLanguage());
        }
        System.out.println();

        return maxString;
    }


    public static String klasyfikacjaInputUsera(List<Perceptron> perceptrons, String text) {

        Map<Character, Integer> map = FileHandling.makeMap();
        text.chars()
                .mapToObj(c -> (char) c)
                .filter(map::containsKey)
                .forEach(c -> map.computeIfPresent(c, (key, value) -> value + 1));

        Obserwacja obserwacja = FileHandling.getObserwacja(null, map);

        return getResultFromPerceptron(perceptrons, obserwacja.getListaAtrybutowWarunkowych());


    }
}
