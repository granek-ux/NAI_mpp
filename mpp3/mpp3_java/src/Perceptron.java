import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Perceptron {
    private List<Double> weights;
    private double threshold;
    private String language;
    private int sizeOfWeights;
    private final double learningConstant =  0.1;
    private final double wantedError = 0.001;

    public Perceptron(String language, List<Obserwacja> trainingData) {
        this.language = language;
        sizeOfWeights = trainingData.getFirst().getListSize();
        randomiseWeightsThershold();

        LearnData(trainingData);
    }

    public double Compute(List<Double> inputs) {
        double net = IntStream.range(0, inputs.size()).mapToDouble(i -> weights.get(i) * inputs.get(i)).sum();

        return 1 / (1 + Math.exp( -net) );
    }
    private boolean Learn(List<Double> inputs, double decision) {
        double y = Compute(inputs);
        double error = 1./2. * Math.pow ((decision - y ), 2);
        if (error < wantedError) return true;

        double stalaPoprawki = learningConstant * (decision - y) * y * ( 1 - y);

        IntStream.range(0, weights.size()).forEach(i -> weights.set(i, weights.get(i) + stalaPoprawki  * inputs.get(i)));


        return false;
    }

    public void LearnData(List<Obserwacja> trainingData) {
        boolean czySieZgadza = false;
        int counter = 0;
        while (!czySieZgadza) {
            czySieZgadza = true ;
            for (Obserwacja o : trainingData) {
                if (!Learn(o.getListaAtrybutowwarunkowych(), o.getAtrybutDecyzyjny().equals(language) ? 1 : 0))
                    czySieZgadza = false;

            }
            counter++;
        }
//        System.out.println("Perceptron z językiem " + language + " uczył sie tyle epok: " + counter);
    }

    private void randomiseWeightsThershold()
    {
        Random random = new Random();
        this.threshold = random.nextDouble();

        this.weights = Stream.generate(random::nextDouble)
                .limit(sizeOfWeights)
                .collect(Collectors.toList());
    }

    public String getLanguage() {
        return language;
    }
}
