
public final class Item implements Comparable<Item>{
    private final int id;
    private static int nextId = 1;
    private final int size;
    private final int vals;
    private final double density;

    public Item(int size, int vals) {
        id = nextId++;
        this.size = size;
        this.vals = vals;
        this.density = (double) this.vals / this.size;
    }

    public Double getSize() {
        return (double) size;
    }

    public Double getVals() {
        return (double) vals;
    }

    @Override
    public int compareTo(Item o) {
        return Double.compare(this.density, o.density);
    }

    @Override
    public String toString() {
        return "(id: " + id + " s: " + size + ", v: " + vals + ")";
    }
}
