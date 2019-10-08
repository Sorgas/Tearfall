package stonering.entity.unit.aspects.health;

import stonering.entity.unit.Unit;

/**
 * Decreases some {@link HealthAspect} property over time.
 *
 * @author Alexander on 08.10.2019.
 */
public class HealthTimedBuff extends HealthBuff {

    public HealthTimedBuff(String tag, int delta, String propertyName, int x, int y) {
        super(tag, delta, propertyName, x, y);
    }

    public HealthTimedBuff(String tag, int delta, String propertyName) {
        super(tag, delta, propertyName);
    }

    @Override
    public boolean decrease(Unit unit) {
        applyDeltaToProperty(unit, 1);
        return super.decrease(unit);
    }

    @Override
    public boolean apply(Unit unit) {
        return true;
    }

    @Override
    public boolean unapply(Unit unit) {
        return true;
    }
}
