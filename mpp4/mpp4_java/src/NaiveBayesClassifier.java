import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NaiveBayesClassifier {
    private final String testFileName;
    private final List<Obserwacja> traningobserwacje;
    private final Map<String, Long> outcomesQuantyty;

    public NaiveBayesClassifier(String treningFileName, String testFileName) {
        this.testFileName = testFileName;
        traningobserwacje = FileHandling.readTrainingFile(treningFileName);

        outcomesQuantyty = traningobserwacje.stream()
                .collect(Collectors.groupingBy(Obserwacja::getAtrybutDecyzyjny, Collectors.counting()));
        test();
        confusionMatrix();
    }

    public String classify(Obserwacja obserwacja) {
        Map<String, List<Long>> namedQuatity = new HashMap<>();
        Map<String, Double> possibility = new HashMap<>();

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

        Set<Integer> toChange = getSetIntegers(namedQuatity);

        //przed wygładzeniem
        for (String possibleOutcome : namedQuatity.keySet()) {
            double wynik = 1;
            for (int i = 0; i < namedQuatity.get(possibleOutcome).size(); i++) {
                if (i == namedQuatity.get(possibleOutcome).size() - 1)
                    wynik *= (double) namedQuatity.get(possibleOutcome).get(i) / traningobserwacje.size();
                else
                    wynik *= (double) namedQuatity.get(possibleOutcome).get(i) / outcomesQuantyty.get(possibleOutcome);


            }
        System.out.println("prawdopodobienstwo na " + possibleOutcome + " " + wynik + "  przed wygladzeniem");
        }

        //po wygladzeniu
        for (String possibleOutcome : namedQuatity.keySet()) {
            double wynik = 1;
            for (int i = 0; i < namedQuatity.get(possibleOutcome).size(); i++) {


                if (toChange.contains(i))
                    wynik *= (double) (namedQuatity.get(possibleOutcome).get(i) + 1) / (outcomesQuantyty.get(possibleOutcome) + outcomesQuantyty.size());
                else if (i == namedQuatity.get(possibleOutcome).size() - 1)
                    wynik *= (double) namedQuatity.get(possibleOutcome).get(i) / traningobserwacje.size();
                else
                    wynik *= (double) namedQuatity.get(possibleOutcome).get(i) / outcomesQuantyty.get(possibleOutcome);

                System.out.println("prawdopodobienstwo na " + possibleOutcome + " " + wynik + "  po wygladzeniu");
            }

            possibility.put(possibleOutcome, wynik);
        }

        return possibility.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }

    private static Set<Integer> getSetIntegers(Map<String, List<Long>> namedQuatity) {
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

        if (toChange.isEmpty()) {
            toChange.add(0);
        }
        return toChange;
    }


    public void test()
    {
        List<Obserwacja> obs = FileHandling.readTestFile(testFileName);
        int couter = 0;
        for( Obserwacja obserwacja: obs)
        {
            if (classify(obserwacja).equals(obserwacja.getAtrybutDecyzyjny()))
                couter++;
        }

        System.out.println(couter + " out of " + obs.size());

        double procent = (double) couter / obs.size();

       procent = Math.round(procent * 100.0);

       System.out.println("procent = " + procent + "%");
    }

    public void confusionMatrix() {
        List<Obserwacja> obs = FileHandling.readTestFile(testFileName);
        Map<String, Map<String, Integer>> matrix = new HashMap<>();

        for (Obserwacja obserwacja : obs) {
            String actual = obserwacja.getAtrybutDecyzyjny();
            String predicted = classify(obserwacja);

            // Initialize rows and cells in the matrix if necessary
            matrix.putIfAbsent(actual, new HashMap<>());
            Map<String, Integer> row = matrix.get(actual);
            row.put(predicted, row.getOrDefault(predicted, 0) + 1);
        }

        // Get all unique classes (actual + predicted)
        Set<String> allClasses = new TreeSet<>();
        for (String actual : matrix.keySet()) {
            allClasses.add(actual);
            allClasses.addAll(matrix.get(actual).keySet());
        }

        // Print the matrix
        System.out.println("Confusion Matrix:");
        System.out.print(String.format("%15s", ""));
        for (String predicted : allClasses) {
            System.out.print(String.format("%15s", predicted));
        }
        System.out.println();

        for (String actual : allClasses) {
            System.out.print(String.format("%15s", actual));
            for (String predicted : allClasses) {
                int count = matrix.getOrDefault(actual, new HashMap<>()).getOrDefault(predicted, 0);
                System.out.print(String.format("%15d", count));
            }
            System.out.println();
        }
    }


    public int atributesSize() {

        return traningobserwacje.getFirst().getListaAtrybutowWarunkowych().size();
    }
}
