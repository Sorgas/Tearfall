package stonering.entity.local.item.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;
import stonering.entity.local.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Entities with this aspect can hold item.
 */
public class ItemContainerAspect extends Aspect {
    public static final String NAME = "item_container";
    private String itemType; //TODO move to enum
    private int volume;
    private List<Item> items;

    public ItemContainerAspect(Entity entity) {
        super(entity);
        items = new ArrayList<>();
    }

    @Override
    public void turn() {
        items.forEach(Entity::turn);
    }

    public List<Item> getItems() {
        return items;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
