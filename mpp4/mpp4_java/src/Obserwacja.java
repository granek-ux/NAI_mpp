import java.util.List;

public class Obserwacja {
    private final String atrybutDecyzyjny;
    private final List<String> listaAtrybutowWarunkowych;

    public Obserwacja(String atrybutDecyzyjny, List<String> listaAtrybutowWarunkowych) {
        this.atrybutDecyzyjny = atrybutDecyzyjny;
        this.listaAtrybutowWarunkowych = listaAtrybutowWarunkowych;
    }

    public String getAtrybutDecyzyjny() {
        return atrybutDecyzyjny;
    }

    public List<String> getListaAtrybutowWarunkowych() {
        return listaAtrybutowWarunkowych;
    }


    public void setAtrybutWarunkowy(int i, String name ) {
        listaAtrybutowWarunkowych.set(i, name);
    }
}
