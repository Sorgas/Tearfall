package stonering.generators.items;

import stonering.enums.items.ItemType;
import stonering.enums.items.ItemTypeMap;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.generators.aspect.AspectGenerator;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.items.Item;

/**
 * @author Alexander Kuzyakov on 26.01.2018.
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

    public Item generateItem(String title, int material) throws DescriptionNotFoundException {
        if (MaterialMap.getInstance().hasMaterial(material)) {
            Item item = generateItem(title);
            item.setMaterial(material);
            return item;
        }
        throw new DescriptionNotFoundException("type: Item, title: " + title + " material: " + material);
    }

    private Item createItem(ItemType itemType) {
        Item item = new Item(null);
        item.setTitle(itemType.getTitle());
        item.setType(itemType);
        itemType.getAspects().keySet().forEach(aspectName -> AspectGenerator.createAspect(aspectName, item).ifPresent(item::addAspect));
        return item;
    }
}
