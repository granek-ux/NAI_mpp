import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHandling {
    private Map<String, List<String>> trainingDataFiles;
    private List<Obserwacja> obserwacje;
    private Set<String> languages;


    public FileHandling(String filePath) {
        trainingDataFiles = new LinkedHashMap<>();
        languages = new TreeSet<>();
        readTrainingDatadirToMap(filePath);
        obserwacje = new ArrayList<>();
        makeObserwacjaList();
        obserwacje.forEach(System.out::println);
    }

    private void readTrainingDatadirToMap(String dirName) {
        try {
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException, IOException {

                    String language = file.getParent().getFileName().toString();
                    String fileName = file.toFile().getAbsolutePath();

                    trainingDataFiles.computeIfAbsent(language, k -> {
                        languages.add(k);
                        return new ArrayList<>();
                    }).add(fileName);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Obserwacja readFile(String language, String fileName) {
        Map<Character,Integer> map = makeMap();
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEach(line -> line.chars()
                    .mapToObj(c -> (char) c)
                    .filter(map::containsKey)
                    .forEach(c -> map.computeIfPresent(c, (key, value) -> value + 1)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return getObserwacja(language, map);
    }

    public static Obserwacja getObserwacja(String language, Map<Character, Integer> map) {
        int sum = map.values().stream().mapToInt(Integer::intValue).sum();

        List<Double> list = map.values().stream()
                .map(value -> value / (double) sum)
                .collect(Collectors.toList());

        return new Obserwacja(language, list);
    }

    private void makeObserwacjaList()
    {
        trainingDataFiles.forEach((key, value) -> {
            value.forEach(fileName -> {
               obserwacje.add(readFile(key,fileName));
            });
        });

        Collections.shuffle(obserwacje);
    }

    public static TreeMap<Character, Integer> makeMap ()
    {
        TreeMap<Character, Integer> map = new TreeMap<>();
        for ( int i = 97; i <= 122; i++ )
            map.put((char)i, 0);
        return map;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public List<Obserwacja> getObserwacje() {
        return obserwacje;
    }
}
