import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Perceptron {
    private List<Double> weights;
    private double threshold;
    private String testFileName;
    private String trainingFileName;
    private List<Obserwacja> trainingData;
    private double learningConstant;

//    public Perceptron(List<Double> weights, double threshold, String testFileName, String trainingFileName) {
//        this.weights = weights;
//        this.threshold = threshold;
//        this.testFileName = testFileName;
//        this.trainingFileName = trainingFileName;
//        learningConstant = 0.1;
//        this.trainingData = ReadFile(trainingFileName);
//    }

    public Perceptron(String testFileName, String trainingFileName) {
        this.testFileName = testFileName;
        this.trainingFileName = trainingFileName;
        this.learningConstant = 0.1;
        this.trainingData = ReadFile(trainingFileName);
    }

    public void Start(List<Double> weights, double threshold)
    {
        this.weights = weights;
        this.threshold = threshold;
        LearnData();
        Test();
    }



    public int Compute(List<Double> inputs) {
        double net = IntStream.range(0, inputs.size()).mapToDouble(i -> weights.get(i) * inputs.get(i)).sum();

        return net >= threshold ? 1 : 0;
    }

    private boolean Learn(List<Double> inputs, int decision) {
        int y = Compute(inputs);
        if (y == decision) return true;

        double stalaPoprawki = (decision - y) * learningConstant;

        IntStream.range(0, weights.size()).forEach(i -> weights.set(i, weights.get(i) + stalaPoprawki * inputs.get(i)));

        return false;
    }

    public List<Obserwacja> ReadFile(String filename) {
        try {
            return Files.lines(Paths.get(filename)).map(line -> {

                String[] tokens = line.trim().replaceAll(",", ".").split("\\s+");
                List<Double> tmplist = new ArrayList<>();

                for (int i = 0; i < tokens.length - 1; i++)
                    if (!tokens[i].isEmpty()) tmplist.add(Double.parseDouble(tokens[i]));

                return new Obserwacja(tokens[tokens.length - 1], tmplist);
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void LearnData() {
        AtomicBoolean czySieZgadza = new AtomicBoolean(false);
        AtomicInteger counter = new AtomicInteger(0);
        while (!czySieZgadza.get()) {
            czySieZgadza.set(true);
            trainingData.forEach(o -> {
                if (!Learn(o.getListaAtrybutowwarunkowych(), o.getAtrybutDecyzyjny().equals("Iris-setosa") ? 1 : 0))
                    czySieZgadza.set(false);

                counter.set(counter.get() + 1);
            });

        }
        System.out.println(counter.get());

    }

    public void Test() {
        List<Obserwacja> testData = ReadFile(testFileName);

        AtomicInteger counterGood = new AtomicInteger(0);

        testData.forEach(o -> {
            int yt = o.getAtrybutDecyzyjny().equals("Iris-setosa") ? 1 : 0;

           int y = Compute(o.getListaAtrybutowwarunkowych());
           if (yt == y) counterGood.incrementAndGet();
        });

        System.out.println("ilosc poprawnych danych: " +counterGood.get());
        double percent = (double) counterGood.get() / testData.size() * 100;
        System.out.println("Procent poprawnych danych: " + percent + "%");
    }

    public int getSizeTrainingData() {
        return trainingData.getFirst().getListaAtrybutowwarunkowych().size();
    }
}
