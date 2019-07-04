package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Determines trading value. Items without this aspect cannot be traded.
 *
 * @author Alexander on 13.06.2019.
 */
public class ValueAspect extends Aspect {
    private float valueModifier;

    public ValueAspect(Entity entity, float valueModifier) {
        super(entity);
        this.valueModifier = valueModifier;
    }

    public float getValueModifier() {
        return valueModifier;
    }
}
