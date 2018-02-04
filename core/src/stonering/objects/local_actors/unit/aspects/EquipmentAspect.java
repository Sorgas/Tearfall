package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.aspects.PropertyAspect;

import java.util.ArrayList;

/**
 * Created by Alexander on 03.01.2018.
 *
 * manages all items equipped by unit
 */
public class EquipmentAspect extends Aspect {
    private ArrayList<Item> items;

    public EquipmentAspect(AspectHolder aspectHolder) {
        super("equipment", aspectHolder);
        items = new ArrayList<>();
    }

    public Item getItemWithAspectAndProperty(String aspectName, String property) {
        for (Item item : items) {
            if (item.getAspects().keySet().contains(aspectName)) {
                if (((PropertyAspect) item.getAspects().get(aspectName)).getProperties().contains(property))
                    return item;
            }
        }
        return null;
    }

    public void equipItem(Item item) {
        if (item.getAspects().containsKey("tool")) {
            items.add(item);
            gameContainer.getItemContainer().removeItem(item);
        }
    }
}
