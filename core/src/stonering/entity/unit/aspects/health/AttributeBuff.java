package stonering.entity.unit.aspects.health;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.AttributeAspect;
import stonering.enums.unit.Attributes;
import stonering.util.global.Logger;

/**
 * Holds delta value for attribute.
 *
 * @author Alexander on 16.09.2019.
 */
public class AttributeBuff extends Buff {
    public final Attributes attribute; // buff is applied to this attribute

    public AttributeBuff(Attributes attribute, int amount) {
        super(amount);
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
        AttributeAspect aspect = unit.getAspect(AttributeAspect.class);
        if(aspect == null) {
            Logger.UNITS.logError("Trying to apply buff " + this + " to unit " + unit + " with no AttributeAspect");
            return false;
        }
        aspect.update(attribute, delta * multiplier); // will call other systems to update characteristics
        return true;
    }
}
