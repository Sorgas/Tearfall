package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * @author Alexander on 6/11/2020
 */
public class FoodItemAspect extends Aspect {
    public int nutrition;

    public FoodItemAspect(Entity entity, int nutrition) {
        super(entity);
        this.nutrition = nutrition;
    }
}
