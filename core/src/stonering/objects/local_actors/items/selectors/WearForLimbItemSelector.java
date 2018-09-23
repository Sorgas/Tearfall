package stonering.objects.local_actors.items.selectors;

import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;

/**
 * Selects wear items, which can cover specified limb.
 *
 * @author Alexander on 21.09.2018.
 */
public class WearForLimbItemSelector extends ItemSelector {
    private String limbName;

    public WearForLimbItemSelector(String limbName) {
        this.limbName = limbName;
    }

    @Override
    public boolean check(ArrayList<Item> items) {
        for (Item item : items) {
            if (item.isWear() && item.getType().getWear().getAllBodyParts().contains(limbName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Item> selectItems(ArrayList<Item> items) {
        ArrayList<Item> selectedItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isWear() && item.getType().getWear().getAllBodyParts().contains(limbName)) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public String getLimbName() {
        return limbName;
    }

    public void setLimbName(String limbName) {
        this.limbName = limbName;
    }
}
