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
    private final double wantedError = 0.01;

    public Perceptron(String language, List<Obserwacja> trainingData) {
        this.language = language;
        sizeOfWeights = trainingData.getFirst().getListSize();
        randomiseWeights();

        learnData(trainingData);
    }

    public double compute(List<Double> inputs) {
        double net = IntStream.range(0, inputs.size()).mapToDouble(i -> weights.get(i) * inputs.get(i)).sum();

        return (2 / (1 + Math.exp( -net) ) -1) ;
    }
    private Double learn(List<Double> inputs, double decision) {
        double y = compute(inputs);
        double error = 1./2. * Math.pow ((decision - y ), 2);

        double stalaPoprawki = learningConstant * 0.5 * (decision - y) * ( 1 - Math.pow(y,2));

        IntStream.range(0, weights.size()).forEach(i -> weights.set(i, (weights.get(i) + stalaPoprawki  * inputs.get(i) )));


        return error;
    }

    public void learnData(List<Obserwacja> trainingData) {
        boolean isCorrect = false;
        int counter = 0;
        while (!isCorrect) {
            double totalError =0.;
            for (Obserwacja o : trainingData) {
                 totalError += learn(o.getListaAtrybutowWarunkowych(), o.getAtrybutDecyzyjny().equals(language) ? 1 : -1);
            }

            isCorrect = totalError/trainingData.size()  < wantedError;
            counter++;
        }
        System.out.println("Perceptron z językiem " + language + " uczył sie tyle epok: " + counter);
    }

    private void randomiseWeights()
    {
        Random random = new Random();

        this.weights = Stream.generate(random::nextDouble)
                .limit(sizeOfWeights)
                .collect(Collectors.toList());

    }


    public static void normalizeVector (List<Double> vector ) {

        double norm = vector.stream().mapToDouble(e -> Math.pow(e,2)).sum();
        norm = Math.sqrt(norm);

        for(int i = 0; i < vector.size(); i++) {
            vector.set(i, vector.get(i) / norm);
        }
    }

    public String getLanguage() {
        return language;
    }
}
