package stonering.util.math;

/**
 * @author Alexander on 26.08.2019.
 */
public class MathUtil {
    public static int toRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}
