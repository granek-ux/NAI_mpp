import java.util.List;

public class Obserwacja {
    private final String atrybutDecyzyjny;
    private final List<Double> listaAtrybutowwarunkowych;

    public Obserwacja(String atrybutDecyzyjny, List<Double> listaAtrybutowwarunkowych) {
        this.atrybutDecyzyjny = atrybutDecyzyjny;
        this.listaAtrybutowwarunkowych = listaAtrybutowwarunkowych;
    }

    public Obserwacja(List<Double> listaAtrybutowwarunkowych) {
        this.listaAtrybutowwarunkowych = listaAtrybutowwarunkowych;
        this.atrybutDecyzyjny = null;
    }

    public List<Double> getListaAtrybutowwarunkowych() {
        return listaAtrybutowwarunkowych;
    }

    public String getAtrybutDecyzyjny() {
        return atrybutDecyzyjny;
    }

    public int getListSize ()
    {
        return listaAtrybutowwarunkowych.size();
    }

    @Override
    public String toString() {
        return "Obserwacja{" +
                "atrybutDecyzyjny='" + atrybutDecyzyjny + '\'' +
                ", listaAtrybutowwarunkowych=" + listaAtrybutowwarunkowych +
                '}';
    }
}
