package stonering.entity.unit.aspects.health;

import stonering.entity.unit.Unit;
import stonering.util.logging.Logger;

/**
 * Modifies property in {@link HealthAspect}.
 *
 * @author Alexander on 16.09.2019.
 */
public class HealthBuff extends Buff {
    public final String propertyName;

    public HealthBuff(String tag, float delta, String propertyName, int x, int y) {
        super(tag, delta, x, y);
        this.propertyName = propertyName;
    }

    public HealthBuff(String tag, int delta, String propertyName) {
        super(tag, delta);
        this.propertyName = propertyName;
    }

    @Override
    public boolean apply(Unit unit) {
        return applyDeltaToProperty(unit, 1);
    }

    @Override
    public boolean unapply(Unit unit) {
        return applyDeltaToProperty(unit, -1);
    }

    protected boolean applyDeltaToProperty(Unit unit, int multiplier) {
        HealthAspect aspect = unit.get(HealthAspect.class);
        if (aspect == null)
            return Logger.UNITS.logError("Trying to apply HealthBuff " + this + " to creature " + unit + " with no HealthAspect", false);
        if (!aspect.properties.containsKey(propertyName))
            return Logger.UNITS.logError("HealthAspect of unit " + unit + " has no property " + propertyName, false);
        aspect.properties.put(propertyName, aspect.properties.get(propertyName) + delta * multiplier); // success
        return true;
    }
}
