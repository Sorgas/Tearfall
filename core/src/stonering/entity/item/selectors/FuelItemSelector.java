package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.FuelAspect;

/**
 * @author Alexander on 18.09.2019.
 */
public class FuelItemSelector extends ItemSelector {

    @Override
    public boolean checkItem(Item item) {
        return item.hasAspect(FuelAspect.class) && item.getAspect(FuelAspect.class).isEnabled();
    }
}
