package stonering.util.geometry;

/**
 * Represents continuous range of a value.
 * 
 * @author Alexander on 15.06.2020.
 */
public class ValueRange {
    public Float min;
    public Float max;

    public ValueRange(Float min, Float max) {
        this.min = min != null ? min : Float.NEGATIVE_INFINITY;
        this.max = max != null ? max : Float.POSITIVE_INFINITY;
    }

    public boolean check(float value) {
        return value >= min && value <= max;
    }
}
