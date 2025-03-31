import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static final String testFileName = "../daneTestowe";
    static final String traning = "../daneTreningowe";

    public static void main(String[] args) {

        System.out.println("Pomiaru czas start");

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
                .map(obs -> obs.getAtrybutDecyzyjny().equalsIgnoreCase(getResultFromPerceptron(perceptrons, obs.getListaAtrybutowwarunkowych())) ? 1 : 0)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static String getResultFromPerceptron(List<Perceptron> perceptrons, List<Double> inputs) {
        return perceptrons.stream()
                .max(Comparator.comparingDouble(p -> p.Compute(inputs)))
                .map(Perceptron::getLanguage)
                .orElse("");
//
//        System.out.println("nowyyyyyyyy");
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//
//        for (Perceptron perceptron : perceptrons) {
//            System.out.println(perceptron.Compute(inputs) + " " + perceptron.getLanguage());
//        }
//
//        return "";
    }


    public static String kasyfikacjaInputUsera(List<Perceptron> perceptrons, String text) {

        Map<Character, Integer> map = FileHandling.makeMap();
        text.chars()
                .mapToObj(c -> (char) c)
                .filter(map::containsKey)
                .forEach(c -> map.computeIfPresent(c, (key, value) -> value + 1));

        Obserwacja obserwacja = FileHandling.getObserwacja(null, map);

        return getResultFromPerceptron(perceptrons, obserwacja.getListaAtrybutowwarunkowych());


    }
}
