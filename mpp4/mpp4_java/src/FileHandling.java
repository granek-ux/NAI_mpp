import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileHandling {
    private static List<List<Range>> ranges = new ArrayList<>();


    public static List<Obserwacja> readTrainingFile(String fileName) {
        List<Obserwacja> obserwacje;
        List<List<Double>> listOfListsOfValuesInColumn = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            obserwacje = lines
                    .map(line -> {

                        String[] split = line.trim().replaceAll(",", ".").split("\\s+");

                        if (listOfListsOfValuesInColumn.isEmpty()) {
                            for (int i = 0; i < split.length - 1; i++) {
                                listOfListsOfValuesInColumn.add(new ArrayList<>());
                            }
                        }

                        for (int i = 0; i < split.length - 1; i++) {
                            double value = Double.parseDouble(split[i]);
                            listOfListsOfValuesInColumn.get(i).add(value);
                        }

                        List<String> attributes = Arrays.asList(split).subList(0, split.length - 1);

                        return new Obserwacja(split[split.length - 1], attributes);
                    }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ranges = listOfListsOfValuesInColumn.stream().map(Range::group).collect(Collectors.toList());

        obserwacje.forEach(obs -> IntStream.range(0, ranges.size()).forEach(i -> {
            double value = Double.parseDouble(obs.getListaAtrybutowWarunkowych().get(i));
            String rangeLabel = Range.chooseRange(value, ranges.get(i));
            obs.setAtrybutWarunkowy(i, rangeLabel);
        }));
        Collections.shuffle(obserwacje);
        return obserwacje;
    }

    public static List<Obserwacja> readTestFile(String fileName) {
        List<Obserwacja> obserwacje;

        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            obserwacje = lines
                    .map(line -> {

                        String[] split = line.trim().replaceAll(",", ".").split("\\s+");

                        List<String> attributes = IntStream.range(0, split.length - 1)
                                .mapToObj(i -> {
                                    double value = Double.parseDouble(split[i]);
                                    return Range.chooseRange(value, ranges.get(i));
                                })
                                .collect(Collectors.toList());

                        return new Obserwacja(split[split.length - 1], attributes);
                    }).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return obserwacje;
    }

    public static Obserwacja changeForRange (List<Double> values, String name)
    {

        List<String> attributes = IntStream.range(0, values.size())
                .mapToObj(i -> Range.chooseRange(values.get(i), ranges.get(i)))
                .collect(Collectors.toList());

     return new Obserwacja(name, attributes);
    }

    public static List<Integer> quntytyOfRanges ()
    {

       return ranges.stream().map( e-> {
            return e.size();
        }).collect(Collectors.toList());

    }
}
