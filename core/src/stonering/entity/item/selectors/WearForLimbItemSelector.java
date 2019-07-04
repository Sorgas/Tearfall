package stonering.entity.item.selectors;

import stonering.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Selects wear item, which can cover specified limb.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public class WearForLimbItemSelector extends ItemSelector {
    private String limbName;

    public WearForLimbItemSelector(String limbName) {
        this.limbName = limbName;
    }

    @Override
    public boolean checkItem(Item item) {
        return item.isWear() && item.getType().wear.getAllBodyParts().contains(limbName);
    }
}
