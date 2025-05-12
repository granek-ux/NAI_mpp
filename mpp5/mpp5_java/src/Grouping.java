import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Grouping {

    List<Observation> observations;
    List<Cluster> clusters = new ArrayList<>();
    private int k;

    public Grouping(int k, String filename) {
        this.k = k;
        try {
            observations = FileHandling.readFile(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean endFinalLoop = false;
        while (!endFinalLoop) {

            InitializeClusters();
            boolean endGrouping = false;
            boolean endCluster = false;
             while (!endGrouping && !endCluster) {
                endGrouping = group();
                endCluster = setClustersPositions();
                showDistances();
                endFinalLoop = checkIfAnyClusterIsEmpty();

             }
        }
        showResult();
    }

    private void InitializeClusters() {
        Set<Integer> selectedIndices = new HashSet<>();
        Random rand = new Random();

        while (selectedIndices.size() < k) {
            int id = rand.nextInt(observations.size());
            if (selectedIndices.add(id)) {
                clusters.add(new Cluster(observations.get(id).getList()));
            }
        }
    }

    private boolean group() {
        AtomicBoolean isEnded = new AtomicBoolean(true);
        observations.forEach(observation -> {
            Map<Integer, Double> namedDistance = new HashMap<>();
            for (Cluster cluster : clusters) {
                namedDistance.put(cluster.getId(), calculateDistance(observation.getList(), cluster.getAtribute()));
            }
            int id = namedDistance.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(-1);
            if (observation.getClasterId() != id) {
                observation.setClasterId(id);
                isEnded.set(false);
            }
        });
        return isEnded.get();
    }

    private boolean setClustersPositions() {
        boolean isEnded = true;
        for (Cluster cluster : clusters) {
            List<List<Double>> list = observations.stream()
                    .filter(observation -> observation.getClasterId() == cluster.getId())
                    .map(Observation::getList).toList();
            int n = list.getFirst().size();

            List<Double> newList = new ArrayList<>();
            List<Double> meanVector = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                final int index = i;
                double avg = list.stream()
                        .mapToDouble(vector -> vector.get(index))
                        .average()
                        .orElse(0.0);
                newList.add(avg);
            }
            if (!cluster.getAtribute().equals(newList)) {
                cluster.setAtribute(newList);
                isEnded = false;
            }
        }

        return isEnded;
    }

    private void showDistances()
    {
        StringBuilder sb = new StringBuilder();
        for (Cluster cluster : clusters) {
            double sum = 0.0;
            sb.append("Claster nr. ").append(cluster.getId()).append(System.lineSeparator());

            List<List<Double>> filtredList = observations.stream().filter(observation -> observation.getClasterId() == cluster.getId()).map(Observation::getList).toList();

            for (List<Double> vector : filtredList) {
                sum += Math.pow(calculateDistance(vector, cluster.getAtribute()),2);
            }

            sb.append("suma kwadratów od centroida: ").append(sum).append(System.lineSeparator());
        }

        System.out.println(sb.toString());
    }

    private boolean checkIfAnyClusterIsEmpty()
    {
        for (Cluster cluster : clusters) {

            long quantity = observations.stream().filter(observation -> observation.getClasterId() == cluster.getId()).count();
            if (quantity == 0)
                return false;
        }
        return true;
    }

    private void showResult()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Wynik: ").append(System.lineSeparator());

        for (Cluster cluster : clusters) {
            List<Observation> list = observations.stream().filter(observation -> observation.getClasterId() == cluster.getId()).toList();

            Double entropy = calculateEntropy(list);

            sb.append("Cluster nr. ").append(cluster.getId()).append(" ma entropie ").append(entropy).append(" oraz posiada: ").append(System.lineSeparator());

            list.forEach(observation -> {
                sb.append(observation.toString()).append(System.lineSeparator());
            });
            sb.append(System.lineSeparator()).append(System.lineSeparator());

        }
        System.out.println(sb.toString());
    }

    private Double calculateEntropy (List<Observation> observations)
    {
        Map<String, Long> map = observations.stream()
                .collect(Collectors.groupingBy(Observation::getAtribute, Collectors.counting()));


        double entropy = - map.values().stream().mapToDouble(v -> (double) v / observations.size()).map(v -> v * log2(v)).sum();

        return entropy == -0.0 ? 0.0 : entropy;
    }

    public static double log2(double n) {
        return (Math.log(n) / Math.log(2));
    }

    public static double calculateDistance(List<Double> observation, List<Double> cluster) {
        double distance = 0;
        for (int i = 0; i < observation.size(); i++) {
            distance += Math.pow(observation.get(i) - cluster.get(i), 2);
        }
        return Math.sqrt(distance);
    }
}
