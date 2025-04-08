import java.util.List;

public class Obserwacja {
    private final String atrybutDecyzyjny;
    private final List<Double> listaAtrybutowWarunkowych;

    public Obserwacja(String atrybutDecyzyjny, List<Double> listaAtrybutowWarunkowych) {
        this.atrybutDecyzyjny = atrybutDecyzyjny;
        this.listaAtrybutowWarunkowych = listaAtrybutowWarunkowych;
    }

    public Obserwacja(List<Double> listaAtrybutowWarunkowych) {
        this.listaAtrybutowWarunkowych = listaAtrybutowWarunkowych;
        this.atrybutDecyzyjny = null;
    }

    public List<Double> getListaAtrybutowWarunkowych() {
        return listaAtrybutowWarunkowych;
    }

    public String getAtrybutDecyzyjny() {
        return atrybutDecyzyjny;
    }

    public int getListSize ()
    {
        return listaAtrybutowWarunkowych.size();
    }

    @Override
    public String toString() {
        return "Obserwacja{" +
                "atrybutDecyzyjny='" + atrybutDecyzyjny + '\'' +
                ", listaAtrybutowWarunkowych=" + listaAtrybutowWarunkowych +
                '}';
    }
}
