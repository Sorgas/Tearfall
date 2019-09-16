package stonering.util.math;

/**
 * @author Alexander on 26.08.2019.
 */
public class MathUtil {
    public static int toRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static boolean inPercentRange(float val, float base, int lowRange, int highRange) {
        return val > base * lowRange && val <= base * highRange;
    }

    /**
     * Checks that val1 and val2 are both more than range low end and range high end.
     * Range ends are percents of a base value.
     * If lowRange > highRange,
     */
    public static boolean inSamePercentRange(float val1, float val2, float base, int lowRange, int highRange) {
        float low = base * lowRange;
        float high = base * highRange;
        return val1 > low && val1 <= high && val2 > low && val2 <= high;
    }

    public static boolean rangeChanged(float val1, float val2, float base, int range) {
        float border = base * range;
        return (val1 > border && val2 <= border)
                || (val2 > border && val1 <= border);
    }
}
