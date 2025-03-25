import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Pliki {
    private final String plikDanychTreningowych = "iris_training.txt";
    private final String plikDanychTestowych = "iris_test.txt";
    private List<Obserwacja> daneTestowe = new ArrayList<>();
    private List<Obserwacja> daneTreningowe = new ArrayList<>();
    private final int liczbaBliskichSasiadow;

    public Pliki(int liczbaBliskichSasiadow) {
        this.liczbaBliskichSasiadow = liczbaBliskichSasiadow;
        this.daneTreningowe = PobranieDanych(plikDanychTreningowych);
        PorowanieDanych();
    }

    private List<Obserwacja> PobranieDanych(String fileName) {
        List<Obserwacja> dane = new ArrayList<>();
        try {
            Stream<String> linesStream = Files.lines(Paths.get(fileName));

            linesStream.forEach(line -> {
                String[] lines = line.split("\\s+");
                List<Double> tmplist = new ArrayList<>();
                for (int i = 0; i < lines.length - 1; i++) {
                    if (!lines[i].isEmpty()) tmplist.add(Double.parseDouble(lines[i].replaceAll(",", ".")));
                }
                Obserwacja tmpobs = new Obserwacja(lines[lines.length - 1], tmplist);
                dane.add(tmpobs);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dane;
    }

    public void PorowanieDanych() {
        daneTestowe = PobranieDanych(plikDanychTestowych);
        int dobreDane =0;
        for (Obserwacja obs : daneTestowe) {
            String name = DlaJednegoWektora(obs);

            if (name.equals(obs.getAtrybutDecyzyjny()))
                dobreDane++;
        }
        double procent =(double) dobreDane / daneTestowe.size();
        procent = procent * 100;
        
        System.out.println("Ilosc zgodnych danych: " + dobreDane );
        System.out.println("Procent zgadzających się danych testowych z obliczeniami: " + procent + "%");

    }


    public String DlaJednegoWektora(Obserwacja obs) {

        List<NamedDistance> distances = new ArrayList<>();
        for (Obserwacja obs1 : daneTreningowe) {
            distances.add(CaculateDistance(obs1, obs));
        }

        Map<String, Integer> map = getKNearest(distances, liczbaBliskichSasiadow);

        return Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();

    }

    public NamedDistance CaculateDistance(Obserwacja obserwacjaBazaDanych, Obserwacja obsnowy) {
        double distance = Distance(obserwacjaBazaDanych.getListaAtrybutowwarunkowych(), obsnowy.getListaAtrybutowwarunkowych());

        return new NamedDistance(obserwacjaBazaDanych.getAtrybutDecyzyjny(), distance);
    }

    public int getVectorSize() {
        return daneTestowe.getFirst().getListaAtrybutowwarunkowych().size();
    }


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
            } else {
                map.put(minName, 1);
            }
        }
        return map;
    }

}
