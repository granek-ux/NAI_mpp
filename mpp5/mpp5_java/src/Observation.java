import java.util.List;

public class Observation {
    private List<Double> list;
    private String atribute;
    private int clasterId;

    public Observation(List<Double> list) {
        this.list = list;
        atribute = "Unknown";
    }

    public Observation(List<Double> list, String atribute) {
        this.list = list;
        this.atribute = atribute;
    }

    public List<Double> getList() {
        return list;
    }

    public String getAtribute() {
        return atribute;
    }

    public int getClasterId() {
        return clasterId;
    }

    public void setClasterId(int clasterId) {
        this.clasterId = clasterId;
    }

    @Override
    public String toString() {
        return "Observation " + atribute + " wektor: " + list.toString();
    }
}
