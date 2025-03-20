import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Perceptron {
    private List<Double> weights;
    private double threshold;
    private final String testFileName;
    private final List<Obserwacja> trainingData;
    private final double learningConstant;

    public Perceptron(String testFileName, String trainingFileName) {
        this.testFileName = testFileName;
        this.learningConstant = 0.1;
        this.trainingData = ReadFile(trainingFileName);
        randomiseWeightsThershold();
        LearnData();
        Test();
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
        boolean czySieZgadza = false;
        int counter = 0;
        while (!czySieZgadza) {
            czySieZgadza = true ;
            for (Obserwacja o : trainingData) {
                if (!Learn(o.getListaAtrybutowwarunkowych(), o.getAtrybutDecyzyjny().equals("Iris-setosa") ? 1 : 0))
                    czySieZgadza = false;

                counter++;
            }
        }
        System.out.println(counter);

    }

    public void Test() {
        List<Obserwacja> testData = ReadFile(testFileName);

        int counterGood = 0;

        for (Obserwacja o : testData) {
            int yt = o.getAtrybutDecyzyjny().equals("Iris-setosa") ? 1 : 0;

            int y = Compute(o.getListaAtrybutowwarunkowych());
            if (yt == y) counterGood++;
        }

        System.out.println("ilosc poprawnych danych: " +counterGood);
        double percent = (double) counterGood / testData.size() * 100;
        System.out.println("Procent poprawnych danych: " + percent + "%");
    }

    public int getSizeTrainingData() {
        return trainingData.getFirst().getListSize();
    }

    private void randomiseWeightsThershold()
    {
        Random random = new Random();
        this.threshold = random.nextDouble();

        this.weights = Stream.generate(random::nextDouble)
                .limit(getSizeTrainingData())
                .collect(Collectors.toList());
    }
}
