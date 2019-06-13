package stonering.entity.local.items.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;
import stonering.entity.local.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Entities with this aspect can hold items.
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
