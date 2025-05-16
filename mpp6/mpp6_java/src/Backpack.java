import java.util.List;

public class Backpack {
    private final int capacity;
    private final List<Item> dataset;

    public Backpack(int capacity, List<Item> dataset)  {
        this.capacity = capacity;
        this.dataset = dataset;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Item> getDataset() {
        return dataset;
    }
}
