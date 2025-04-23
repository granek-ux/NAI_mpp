import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NaiveBayesClassifier {
    private final String testFileName;
    private final List<Obserwacja> traningobserwacje;
    private final Map<String, Long> outcomesQuantyty;
    private final List<Integer> quntytyOfRanges;

    public NaiveBayesClassifier(String treningFileName, String testFileName) {
        this.testFileName = testFileName;
        traningobserwacje = FileHandling.readTrainingFile(treningFileName);
        quntytyOfRanges = FileHandling.quntytyOfRanges();

        outcomesQuantyty = traningobserwacje.stream()
                .collect(Collectors.groupingBy(Obserwacja::getAtrybutDecyzyjny, Collectors.counting()));
        test();
    }

    public String classify(Obserwacja obserwacja) {
        Map<String, List<Long>> namedQuatity = new HashMap<>();


        //liczenie ile obesrwacji posiada szukane elementy
        for (String possibleOutcome : outcomesQuantyty.keySet()) {
            List<Long> quant = IntStream.range(0, obserwacja.getListaAtrybutowWarunkowych().size())
                    .mapToObj(i -> {
                        String lookingFor = obserwacja.getListaAtrybutowWarunkowych().get(i);

                        return traningobserwacje.stream()
                                .filter(value -> value.getAtrybutDecyzyjny().equals(possibleOutcome))
                                .filter(value -> value.getListaAtrybutowWarunkowych().get(i).equals(lookingFor))
                                .count();
                    })
                    .collect(Collectors.toList());


            quant.add(outcomesQuantyty.get(possibleOutcome));

            namedQuatity.put(possibleOutcome, quant);
        }

        Set<Integer> toChange = new HashSet<>();

        // Prawdopodobieństwo bez wygładzania
        obliczniePrawdopodobienstwa(namedQuatity, toChange).forEach( (k, v) -> System.out.println("prawdopodobienstwo na " + k + " " + v + "  przed wygladzeniem"));

        toChange = PozycjeDoWygladzenia(namedQuatity);

        // Prawdopodobieństwo z wygładzaniem
        Map<String, Double> possibility = obliczniePrawdopodobienstwa(namedQuatity, toChange);

        System.out.println();

        possibility.forEach((k, v) -> System.out.println("prawdopodobienstwo na " + k + " " + v + "  po wygladzeniu"));

        return possibility.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }

    private Map<String, Double> obliczniePrawdopodobienstwa(Map<String, List<Long>> namedQuatity, Set<Integer> toChange) {
        Map<String, Double> possibility = new HashMap<>();
        for (String possibleOutcome : namedQuatity.keySet()) {
            double wynik = 1;
            for (int i = 0; i < namedQuatity.get(possibleOutcome).size(); i++) {

                if (toChange.contains(i))
                    wynik *= (double) (namedQuatity.get(possibleOutcome).get(i) + 1) / (outcomesQuantyty.get(possibleOutcome) + quntytyOfRanges.get(i));
                else if (i == namedQuatity.get(possibleOutcome).size() - 1)
                    wynik *= (double) namedQuatity.get(possibleOutcome).get(i) / traningobserwacje.size();
                else
                    wynik *= (double) namedQuatity.get(possibleOutcome).get(i) / outcomesQuantyty.get(possibleOutcome);
            }

            possibility.put(possibleOutcome, wynik);
        }
        return possibility;
    }

    private static Set<Integer> PozycjeDoWygladzenia(Map<String, List<Long>> namedQuatity) {
        Set<Integer> toChange = new HashSet<>();

        for (Map.Entry<String, List<Long>> entry : namedQuatity.entrySet()) {
            List<Long> values = entry.getValue();
            for (int i = 0; i < values.size() - 1; i++) {
                if (values.get(i) == 0) {
                    toChange.add(i);
                }
            }
        }

        //zrobionie wymagania że: Jeśli nie występują przesłanki do wygładzania – przeprowadzić wygładzanie dla pierwszego atrybutu.
        if (toChange.isEmpty()) toChange.add(0);

        return toChange;
    }

    public void test() {
        List<Obserwacja> testObservations = FileHandling.readTestFile(testFileName);
        Map<String, Map<String, Integer>> confusionMatrix = new HashMap<>();

        int couter = 0;

        for (Obserwacja observation : testObservations) {
            String actual = observation.getAtrybutDecyzyjny();
            String predicted = classify(observation);

            if (actual.equals(predicted)) {
                couter++;
            }

            confusionMatrix
                    .computeIfAbsent(actual, k -> new HashMap<>())
                    .compute(predicted, (k, v) -> v == null ? 1 : v + 1);
        }

        // Macierz pomyłek
        Set<String> allClasses = new TreeSet<>();
        for (String actual : confusionMatrix.keySet()) {
            allClasses.add(actual);
            allClasses.addAll(confusionMatrix.get(actual).keySet());
        }

        System.out.println();
        System.out.println("Macierz pomylek:");
        System.out.printf("%15s", "");
        for (String predicted : allClasses) {
            System.out.printf("%15s", predicted);
        }
        System.out.println();

        for (String actual : allClasses) {
            System.out.printf("%15s", actual);
            for (String predicted : allClasses) {
                int count = confusionMatrix
                        .getOrDefault(actual, new HashMap<>())
                        .getOrDefault(predicted, 0);
                System.out.printf("%15d", count);
            }
            System.out.println();

        }
        System.out.println();
        System.out.println(couter + " out of " + testObservations.size());

        double procent = (double) couter / testObservations.size();

        procent = Math.round(procent * 100.0);

        System.out.println("procent = " + procent + "%");
    }


    public int atributesSize() {
        return traningobserwacje.getFirst().getListaAtrybutowWarunkowych().size();
    }
}
