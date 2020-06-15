package stonering.util.math;

/**
 * Represents continuous range of a value.
 * 
 * @author Alexander on 15.06.2020.
 */
public class ValueRange {
    public final Float min;
    public final Float max;

    public ValueRange(Float min, Float max) {
        this.min = min != null ? min : Float.NEGATIVE_INFINITY;
        this.max = max != null ? max : Float.POSITIVE_INFINITY;
    }
    
    public boolean check(float value) {
        return value > min && value <= max;
    }
}
