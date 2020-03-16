package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.FuelAspect;

/**
 * @author Alexander on 18.09.2019.
 */
public class FuelItemSelector extends ItemSelector {

    @Override
    public boolean checkItem(Item item) {
        return item.has(FuelAspect.class) && item.get(FuelAspect.class).isEnabled();
    }
}
