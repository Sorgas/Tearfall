package stonering.entity.unit.aspects.health;

import stonering.entity.unit.Unit;
import stonering.util.global.Logger;

/**
 * Modifies property in {@link HealthAspect}.
 *
 * @author Alexander on 16.09.2019.
 */
public class HealthBuff extends Buff {
    public final String propertyName;

    public HealthBuff(int delta, String propertyName) {
        super(delta);
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

    private boolean applyDeltaToProperty(Unit unit, int multiplier) {
        HealthAspect aspect = unit.getAspect(HealthAspect.class);
        if (aspect == null) return logAndFail("Trying to apply HealthBuff " + this + " to creature " + unit + " with no HealthAspect");
        if (!aspect.properties.containsKey(propertyName)) return logAndFail("HealthAspect of unit " + unit + " has no property " + propertyName);
        aspect.properties.put(propertyName, aspect.properties.get(propertyName) + delta * multiplier); // success
        return true;
    }

    private boolean logAndFail(String message) {
        Logger.UNITS.logError(message);
        return false;
    }
}
