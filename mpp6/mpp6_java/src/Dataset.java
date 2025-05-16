import java.util.Objects;

public record Dataset(int id, String sizes, String vals) {

    @Override
    public String toString() {
        return "Dataset[" +
                "id=" + id + ", " +
                "sizes=" + sizes + ", " +
                "vals=" + vals + ']';
    }
}
