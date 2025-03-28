import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FileHandling {
    private Map<String, List<String>> trainingDataFiles;
    private final String trainingdirPath = "../daneTestowe";

    public FileHandling() {
        trainingDataFiles = new LinkedHashMap<>();
        readTrainingDatadirToMap(trainingdirPath);
        trainingDataFiles.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
    }

    private void readTrainingDatadirToMap(String dirName) {
        try {
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException, IOException {

                    String language = file.getParent().getFileName().toString();
                    String fileName = file.toFile().getAbsolutePath();

                    if (trainingDataFiles.containsKey(language)) {
                        trainingDataFiles.get(language).add(fileName);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(fileName);
                        trainingDataFiles.put(language, list);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFile(String language, String fileName) {
        Map<Character,Integer> map = makeMap();
        try {
            Files.lines(Paths.get(fileName)).forEach(line -> line.chars().forEach(c -> {
                char c1 = (char) c;
                if (map.containsKey(c1)) {
                    map.put(c1,map.get(c1)+1);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int sum = map.values().stream().mapToInt(Integer::intValue).sum();

        map.values().stream()
                .mapToInt(Integer::intValue)
                .mapToDouble(e -> e / (double) sum)
                .collect(Collectors.toList());


    }



    private static TreeMap<Character, Integer> makeMap ()
    {
        TreeMap<Character, Integer> map = new TreeMap<>();
        for ( int i = 97; i <= 122; i++ )
            map.put((char)i, 0);
        return map;
    }

}
