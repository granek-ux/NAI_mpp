import java.text.Normalizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class tsts {
    public static void main(String[] args) {
//        String language = "Zażółć gęślą jaźń";
//        System.out.println(toAsciiOnly(language));
        makeMap().forEach((k, v) -> {
            System.out.println(k+ " " + v);
        });


    }


    public static String toAsciiOnly(String input) {
        return input.replaceAll("[^\\x00-\\x7F]", "");
    }
    private static TreeMap<Character, Integer> makeMap ()
    {
        TreeMap<Character, Integer> map = new TreeMap<>();
        for ( int i = 97; i <= 122; i++ )
            map.put((char)i, 0);
        return map;
    }
}
