import java.util.List;

public class Perceptron {
    List<Double> weights;
    double threshold;

    public Perceptron(List<Double> weights, double threshold) {
        this.weights = weights;
        this.threshold = threshold;
    }



    public int Compute (List<Double> inputs)
    {
        if (inputs.size() != weights.size())
            return -1;

        double net=0;

        for(int i = 0; i < inputs.size(); i++)
            net += weights.get(i) * inputs.get(i);

//        System.out.println(net);

        if (net >= threshold)
            return 1;
        return 0;
    }
}
