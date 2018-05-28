package stonering.generators.items;

import stonering.enums.items.ItemType;
import stonering.enums.items.ItemTypeMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.objects.local_actors.items.Item;

/**
 * Created by Alexander on 26.01.2018.
 */
public class ItemGenerator {
    private ItemTypeMap itemTypeMap;

    public ItemGenerator() {
        init();
    }

    private void init() {
        itemTypeMap = ItemTypeMap.getInstance();
    }

    public Item generateItem(String title) throws DescriptionNotFoundException {
        if (itemTypeMap.contains(title))
            return createItem(itemTypeMap.getItemType(title));
        throw new DescriptionNotFoundException("type: Item, title: " + title);
    }

    private Item createItem(ItemType itemType) {
        Item item = new Item(null);
        item.setTitle(itemType.getTitle());
        item.setType(itemType);
        itemType.getAspects().forEach(item::addAspect);
        return item;
    }
}
