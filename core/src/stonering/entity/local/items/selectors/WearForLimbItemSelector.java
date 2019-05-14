package stonering.entity.local.items.selectors;

import stonering.entity.local.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Selects wear items, which can cover specified limb.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public class WearForLimbItemSelector extends ItemSelector {
    private String limbName;

    public WearForLimbItemSelector(String limbName) {
        this.limbName = limbName;
    }

    @Override
    public boolean check(List<Item> items) {
        for (Item item : items) {
            if (item.isWear() && item.getType().wear.getAllBodyParts().contains(limbName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Item> selectItems(List<Item> items) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isWear()) {
                if (item.getType().wear.getAllBodyParts().contains(limbName))
                    selectedItems.add(item);
            }
        }
        return selectedItems;
    }
}
