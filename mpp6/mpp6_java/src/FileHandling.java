import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandling {
    public static List<Dataset> ReadDatasetsFromFile(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String[] fistLine = br.readLine().trim().replaceAll("-", " ").replaceAll(",", "").split("\\s+");
            double capacity = Double.parseDouble(fistLine[3]);
            Main.setCapicity((int) capacity);
            String line = "";
            List<Dataset> datasets = new ArrayList<Dataset>();
            while ((line = br.readLine()) != null)
            {
                char idC = line.charAt(8);
                int id = idC - '0';
                String sizes = br.readLine().trim();
                String vals = br.readLine().trim();
                br.readLine();
                datasets.add(new Dataset(id, sizes, vals));
            }
            br.close();
            return datasets;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
