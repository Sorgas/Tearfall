package stonering.entity.unit.aspects.health;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.StatAspect;
import stonering.enums.unit.CreatureAttributesEnum;
import stonering.util.logging.Logger;

/**
 * Holds delta value for attribute.
 *
 * @author Alexander on 16.09.2019.
 */
public class AttributeBuff extends Buff {
    public final CreatureAttributesEnum attribute; // buff is applied to this attribute

    public AttributeBuff(String tag, CreatureAttributesEnum attribute, int delta, int x, int y) {
        super(tag, delta, x, y);
        this.attribute = attribute;
    }

    @Override
    public boolean apply(Unit unit) {
        return updateAttribute(unit, 1);
    }

    @Override
    public boolean unapply(Unit unit) {
        return updateAttribute(unit, -1);
    }

    private boolean updateAttribute(Unit unit, int multiplier) {
        StatAspect aspect = unit.get(StatAspect.class);
        if (aspect == null) {
            Logger.UNITS.logError("Trying to apply buff " + this + " to unit " + unit + " with no AttributeAspect");
            return false;
        }
        aspect.setPrimary(attribute, (int) delta * multiplier); // will call other systems to update characteristics
        return true;
    }
}
