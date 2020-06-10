package stonering.util.geometry;

/**
 * @author Alexander on 04.12.2019.
 */
@FunctionalInterface
public interface VectorFunction {
    void apply(int x, int y, int z);
}
