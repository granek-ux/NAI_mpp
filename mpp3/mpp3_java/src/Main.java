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

        List<Long> czasy = new ArrayList<>();

//        for (int i = 0; i < 2; i++) {

//            long start = System.nanoTime();
//
//            List<Obserwacja> Trainingdata = fh.getObserwacje();
//
//            List<Perceptron> perceptrons = languages.stream()
//                    .map(language -> new Perceptron(language, trainingData))
//                    .collect(Collectors.toList());
//
//            czasy.add(System.nanoTime() - start);
//        }

//        double elapsedTimeSecNormal = czasy.stream().mapToLong(Long::longValue).average().orElse(0);
//
//        elapsedTimeSecNormal /= 1_000_000_000F;
//
//        System.out.println(elapsedTimeSecNormal + " sec bez thread");
//
//
//        czasy.clear();

//        for (int i = 0; i < 2; i++) {

            long start = System.nanoTime();



//            List<Obserwacja> Trainingdata = fh.getObserwacje();

            List<Perceptron> perceptrons = languages.parallelStream()
                    .map(language -> new Perceptron(language, trainingData))
                    .collect(Collectors.toList());

            czasy.add(System.nanoTime()-start);
//        }

        double elapsedTimeSecStream = czasy.stream().mapToLong(Long::longValue).average().orElse(0);
        elapsedTimeSecStream /= 1_000_000_000F;  // Konwersja na sekundy

        System.out.println(elapsedTimeSecStream + " sec z stream");

//        double różnica = elapsedTimeSecNormal - elapsedTimeSecStream;
//
//        System.out.println("Różnica (bez stream - parallelStream): " + różnica + " sec");

        test(perceptrons);

        Scanner sc = new Scanner(System.in);

        while (true)
            kasyfikacjaInputUsera(perceptrons, sc);


    }

    private static void test(List<Perceptron> perceptrons) {
        FileHandling fh = new FileHandling(testFileName);
        List<Obserwacja> testData = fh.getObserwacje();

        //todo
        //zastanowić się czy parallelStream ma sens tu
        //test zrobić jakiś

        int counter = testData.parallelStream()
                .map(obs -> obs.getAtrybutDecyzyjny().equalsIgnoreCase(getResultFromPerceptron(perceptrons, obs.getListaAtrybutowwarunkowych())) ? 1 : 0)
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("ilosc dobrych: " +  counter);

        double percent = ((double) counter / (double) testData.size()) * 100;

        System.out.println("percent = " + percent);

    }

    private static String getResultFromPerceptron(List<Perceptron> perceptrons, List<Double> inputs) {
//        return perceptrons.stream()
//                .max(Comparator.comparingDouble(p -> p.Compute(inputs)))
//                .map(Perceptron::getLanguage)
//                .orElse("");
//
//
        for(Perceptron perceptron : perceptrons)
        {
            System.out.println(perceptron.Compute(inputs) + " " + perceptron.getLanguage());
        }

        return "";
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
