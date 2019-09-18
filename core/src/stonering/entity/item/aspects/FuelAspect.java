package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Shows that this type of items can be used as fuel.
 *
 * @author Alexander_Kuzyakov on 08.07.2019.
 */
public class FuelAspect extends Aspect {

    public FuelAspect(Entity entity) {
        super(entity);
    }

    /**
     * Burning is disabled for furniture and finished goods. It also can be disabled in settings.
     * TODO
     */
    public boolean isEnabled() {
        return true;
    }
}
