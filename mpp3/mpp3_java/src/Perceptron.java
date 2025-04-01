import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Perceptron {
    private List<Double> weights;
    private final String language;
    private final int sizeOfWeights;
    private final double learningConstant =  0.1;
    private final double wantedError = 0.1;
    private double maxNET =Double.MAX_VALUE;

    public Perceptron(String language, List<Obserwacja> trainingData) {
        this.language = language;
        sizeOfWeights = trainingData.getFirst().getListSize();
        randomiseWeightsThershold();

        LearnData(trainingData);
    }

    public double Compute(List<Double> inputs) {
        double net = IntStream.range(0, inputs.size()).mapToDouble(i -> weights.get(i) * inputs.get(i)).sum();

        return net;

    }
    private Double Learn(List<Double> inputs, double decision) {
        double y = Compute(inputs);
        double error = Math.abs(decision - y);

        double stalaPoprawki = learningConstant * (decision - y);

        IntStream.range(0, weights.size()).forEach(i -> weights.set(i, (weights.get(i) + stalaPoprawki  * inputs.get(i) )));

//        weights = normalizeVector(weights);

        return error;
    }

    public void LearnData(List<Obserwacja> trainingData) {
        boolean czySieZgadza = false;
        int counter = 0;
        while (!czySieZgadza) {
            double totalError =0.;
            for (Obserwacja o : trainingData) {
                 totalError += Learn(o.getListaAtrybutowwarunkowych(), o.getAtrybutDecyzyjny().equals(language) ? 1 : 0);
            }

            czySieZgadza = totalError/trainingData.size()  < wantedError;
            counter++;
        }
        System.out.println("Perceptron z językiem " + language + " uczył sie tyle epok: " + counter);
    }

    private void randomiseWeightsThershold()
    {
        Random random = new Random();

        this.weights = Stream.generate(random::nextDouble)
                .limit(sizeOfWeights)
                .collect(Collectors.toList());
    }


    public static List<Double> normalizeVector (List<Double> vector ) {

        double norm = vector.stream().mapToDouble(e -> Math.pow(e,2)).sum();
        norm = Math.sqrt(norm);

        for(int i = 0; i < vector.size(); i++) {
            vector.set(i, vector.get(i) / norm);
        }

        return vector;
    }

    public String getLanguage() {
        return language;
    }
}
