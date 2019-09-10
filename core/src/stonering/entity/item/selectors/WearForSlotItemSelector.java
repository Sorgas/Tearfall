package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;

/**
 * Selects wear item, which can cover specified limb.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public class WearForSlotItemSelector extends ItemSelector {
    private String slotName;

    public WearForSlotItemSelector(String slotName) {
        this.slotName = slotName;
    }

    @Override
    public boolean checkItem(Item item) {
        return item.hasAspect(WearAspect.class) && item.getAspect(WearAspect.class).slot.equals(slotName);
    }
}
