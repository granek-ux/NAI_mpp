import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculations {

    public static double Distance(List<Double> distances1, List<Double> distances2) throws IllegalArgumentException {
        double distance = 0;
        if (distances1.size() != distances2.size()) {
            throw new IllegalArgumentException("Lists have different sizes");
        } else {
            for (int i = 0; i < distances1.size(); i++) {
            distance += Math.pow(distances1.get(i) - distances2.get(i), 2);
            }
            distance = Math.sqrt(distance);
        }
        return distance;
    }

    public static Map<String, Integer> getKNearest(List<NamedDistance> distances, int numberOfK) {
        Map<String, Integer> map = new HashMap<>();
        for (int k = 0; k < numberOfK; k++) {
            double minDistance = Double.MAX_VALUE;
            String minName = "";
            int minPosition = 0;
            for (int i = 0; i < distances.size(); i++) {
                if (distances.get(i).getDistance() < minDistance) {
                    minDistance = distances.get(i).getDistance();
                    minName = distances.get(i).getName();
                    minPosition = i;
                }
            }

            distances.remove(minPosition);
            if (map.containsKey(minName)) {
                map.put(minName, map.get(minName) + 1);
            }else {
                map.put(minName, 1);
            }

        }
        return map;
    }
}
