package stonering.entity.unit.aspects.health;

import stonering.enums.unit.Attributes;

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
}
