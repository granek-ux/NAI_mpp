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
    private String language;
    private int sizeOfWeights;
    private final double learningConstant =  0.1;
    private final double wantedError = 0.01;

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
    private Double Learn(List<Double> inputs, double decision) {
        double y = Compute(inputs);
        double error = 1./2. * Math.pow ((decision - y ), 2);

        double stalaPoprawki = learningConstant * (decision - y) * y * ( 1 - y);

        IntStream.range(0, weights.size()).forEach(i -> weights.set(i, (weights.get(i) + stalaPoprawki  * inputs.get(i) )));
//
//        double norm = Math.sqrt(weights.stream().mapToDouble(w -> w * w).sum());
//
//        IntStream.range(0, weights.size()).forEach(i -> weights.set(i, weights.get(i) / norm));
//
//        double tsts =0;
//
//        for (double t: weights) {
//            tsts += Math.pow(t, 2);
//        }
//        tsts = Math.sqrt(tsts);
//
//        for(int i=0 ; i < weights.size() ; i++ ) {
//            weights.set(i, weights.get(i)/tsts);
//        }

//        double norm = 0.0;
//
//        for (int i = 0; i < weights.size(); i++) {
//            double updatedWeight = weights.get(i) + stalaPoprawki * inputs.get(i);
//            weights.set(i, updatedWeight);
//            norm += updatedWeight * updatedWeight;
//        }
//
//        norm = Math.sqrt(norm);
//
//        for (int i = 0; i < weights.size(); i++) {
//            weights.set(i, weights.get(i) / norm);

//        }



//        weights.forEach(System.out::println);

        return error;
    }

    public void LearnData(List<Obserwacja> trainingData) {
        boolean czySieZgadza = false;
        int counter = 0;
        while (!czySieZgadza) {
            double totalerr =0.;
            for (Obserwacja o : trainingData) {
                 totalerr += Learn(o.getListaAtrybutowwarunkowych(), o.getAtrybutDecyzyjny().equals(language) ? 1 : 0);
            }

            czySieZgadza = totalerr  < wantedError;
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

    public String getLanguage() {
        return language;
    }
}
