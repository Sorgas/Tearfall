package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Entities with this aspect can hold item.
 * Different containers can hold different types of items (liquids, grains).
 * Liquids and grains cannot be mixed with other types (wine and milk).
 *
 * @author Alexander_Kuzyakov
 */
public class  ItemContainerAspect extends Aspect {
    public static final String SOLID = "solid";
    public static final String LIQUID = "liquid"; // items with liquid aspect
    public static final String GRAIN = "grain"; // items with grain aspect
    public final List<String> itemTypes; // any combinaiton of above constants
    public final List<Item> items;

    public ItemContainerAspect(Entity entity) {
        super(entity);
        items = new ArrayList<>();
        itemTypes = new ArrayList<>();
    }
}
