package stonering.entity.unit.aspects.need;

/**
 * State of {@link Need}. When  
 * By default, max value of need is 100, but can be changed for specific units, or by buffs.
 *
 * @author Alexander on 06.10.2019.
 */
public class NeedState {
    private float current = 0;
    public float max = 100;

    public float getRelativeValue() {
        return max == 0 ? 0 : current / max * 100f;
    }

    public boolean setValue(float value) {
        current = Math.max(0, value);
        return current > max;
    }

    public boolean changeValue(float delta) {
        return setValue(current + delta);
    }

    public float get() {
        return current;
    }
}
