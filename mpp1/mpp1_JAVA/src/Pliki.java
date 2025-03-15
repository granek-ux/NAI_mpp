import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Pliki {
    private final String plikDanychTreningowych = "iris_test.txt";
    private final String plikDanychTestowych = "iris_training.txt";
    private List<Obserwacja> daneTreningowe = new ArrayList<>();
    private List<Obserwacja> daneTestowe = new ArrayList<>();
    private final int liczbaBliskichSasiadow;

    public Pliki(int liczbaBliskichSasiadow) {
        this.liczbaBliskichSasiadow = liczbaBliskichSasiadow;
        this.daneTestowe = PobranieDanych(plikDanychTestowych);
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
                    if (!lines[i].isEmpty()) {

                        tmplist.add(Double.parseDouble(lines[i].replaceAll(",", ".")));
                    }
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
        daneTreningowe = PobranieDanych(plikDanychTreningowych);
        int nirgit =0;
        for(Obserwacja obs : daneTestowe) {
            String name = DlaJednegoWektora(obs);

            if(!name.equals(obs.getAtrybutDecyzyjny())) {
                nirgit++;
            }
        }
        double procent = (double) (daneTestowe.size() - nirgit) / daneTestowe.size();

        System.out.println("Procent  zgadzających się danych testowych z obliczeniami: " + procent + "%");

        }


    public String DlaJednegoWektora (Obserwacja obs) {

        List<NamedDistance> distances = new ArrayList<>();
        for (Obserwacja obs1 : daneTestowe) {
            distances.add(CaculateDistance(obs1,obs));
        }

        Map<String, Integer> map = Calculations.getKNearest(distances, liczbaBliskichSasiadow);

        return Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();

    }

    public NamedDistance CaculateDistance(Obserwacja obserwacjaBazaDanych, Obserwacja obsnowy)
    {
        double distance = Calculations.Distance(obserwacjaBazaDanych.getListaAtrybutowwarunkowych(), obsnowy.getListaAtrybutowwarunkowych());

        return new NamedDistance(obserwacjaBazaDanych.getAtrybutDecyzyjny(), distance);
    }

    public int getVectorSize()
    { return daneTreningowe.getFirst().getListaAtrybutowwarunkowych().size(); }




}
