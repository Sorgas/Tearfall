package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;

/**
 * @author Alexander Kuzyakov on 03.01.2018.
 * <p>
 * manages all items equipped by unit
 */
public class EquipmentAspect extends Aspect {
    private ArrayList<Item> items;

    public EquipmentAspect(AspectHolder aspectHolder) {
        super("equipment", aspectHolder);
        items = new ArrayList<>();
    }

    public Item getItemWithAspectAndProperty(String property) {
        for (Item item : items) {
            if (item.getType().getAspects().containsKey(property))
                return item;
        }
        return null;
    }

    public void equipItem(Item item, boolean allowHauling) {
        //TODO check available slots for item
        //TODO chaek hauling
        if (true) {
            items.add(item);
            gameContainer.getItemContainer().removeItem(item);
        }
    }

    public void unequipItem(Item item) {
        items.remove(item);
    }

    public boolean checkItem(Item item) {
        //TODO
        return items.contains(item);
    }
}
