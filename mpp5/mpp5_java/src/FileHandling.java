import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandling {

    public static List<Observation> readFile(String fileName)throws IOException {
          List<Observation> list = Files.lines(Paths.get(fileName)).map(String::trim).filter(l -> !l.isEmpty()).map(line ->
            {
                String[] split = line.replace(',', '.').split("\\s+");
                List<Double> values = new ArrayList<>();

                for (int i = 0; i < split.length - 1; i++) {
                    values.add(Double.parseDouble(split[i]));
                }

                String label = split[split.length - 1];
                return new Observation(values, label);
            }).collect(Collectors.toList());
          Collections.shuffle(list);
          return list;
    }
}
