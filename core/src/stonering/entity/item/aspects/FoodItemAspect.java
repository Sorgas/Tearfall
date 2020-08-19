package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.items.FoodCategoryEnum;

/**
 * Aspect of all edible items. Stores amount of hunger, replenished by item.
 * 
 * @author Alexander on 6/11/2020
 */
public class FoodItemAspect extends Aspect {
    public int nutrition;
    public FoodCategoryEnum category;

    public FoodItemAspect(Entity entity, int nutrition, FoodCategoryEnum category) {
        super(entity);
        this.nutrition = nutrition;
        this.category = category;
    }

    public FoodItemAspect(int nutrition, FoodCategoryEnum category) {
        this.nutrition = nutrition;
        this.category = category;
    }
}
