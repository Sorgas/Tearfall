package stonering.enums.unit.health;

/**
 * Value of a health function of a specific creature. 
 * Creatures are generated with different {@link #base} values, all effects, like diseases and buffs stored (additively) in {@link #current}. 
 * Real {@link #value} is multiplication of two above.
 * 
 * @author Alexander on 10.08.2020.
 */
public class HealthFunctionValue {
    public final float base; // never changes
    public float current = 1f;
    private float value;

    public HealthFunctionValue(float base) {
        this.base = base;
    }
    
    public void changeCurrent(float delta) {
        current += delta;
        value = current * base;
    }
    
    public float value() {
        return value;
    }
}
