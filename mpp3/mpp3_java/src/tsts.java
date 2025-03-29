import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class tsts {
    public static void main(String[] args) {
        String file = "C:\\Users\\jbali\\OneDrive\\Pulpit\\semestr 4\\NAI\\NAI_mpp\\mpp3\\daneTestowe\\Polish\\2.txt";

    }



    private static TreeMap<Character, Integer> makeMap ()
    {
        TreeMap<Character, Integer> map = new TreeMap<>();
        for ( int i = 97; i <= 122; i++ )
            map.put((char)i, 0);
        return map;
    }
}
