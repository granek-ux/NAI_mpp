import java.util.ArrayList;
import java.util.List;

public record Range(String name, double start, double max) {
    private static int numberOfTables = 1;


    public static List<Range> group(List<Double> values) {
        int n = values.size();
        int k = (int) Math.ceil(Math.sqrt(n));
        double max = values.stream().max(Double::compareTo).orElse(0.);
        double min = values.stream().min(Double::compareTo).orElse(0.);
        double h = Math.ceil((max - min) / k);

        List<Range> ranges = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            double start = min + i * h;
            double end = start + h;

            start = Math.round(start * 100.0) / 100.0;
            end = Math.round(end * 100.0) / 100.0;
            //todo przemyslec to czy nie towrzyć jakiegoś nowego przedziału
            if (i == 0)
            {
//                start = Double.NEGATIVE_INFINITY;
            ranges.add(new Range("Minimal Value" + numberOfTables, Double.NEGATIVE_INFINITY, min));
            }
            if (i == k - 1)
            {
//                end = Double.POSITIVE_INFINITY;
                ranges.add(new Range("Max Value" + numberOfTables, max, Double.POSITIVE_INFINITY));
            }

            String name = "Column:" + numberOfTables + "_" + (i + 1);
            ranges.add(new Range(name, start, end));
        }
        numberOfTables++;
        return ranges;
    }

    public static String chooseRange(Double value, List<Range> ranges) {
        Range matched = ranges.stream()
                .filter(r -> value >= r.start() && value < r.max())
                .findFirst()
                .orElse(new Range("OutOfRange", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));


        if(matched.name.equals("OutOfRange")) System.out.println("Out of range");
        return matched.name();
    }


    @Override
    public String toString() {
        return "Range{" +
                "name='" + name + '\'' +
                ", start=" + start +
                ", max=" + max +
                '}';
    }


}
