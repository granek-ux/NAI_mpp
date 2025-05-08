import java.util.List;

public class Cluster {
    private final int id;
    private static int nextId = 1;
    private List<Double> atribute;

    public Cluster(List<Double> atribute)
    {
        id = nextId++;
        this.atribute = atribute;
    }

    public List<Double> getAtribute() {
        return atribute;
    }

    public int getId() {
        return id;
    }

    public void setAtribute(List<Double> atribute) {
        this.atribute = atribute;
    }
}
