
package stonering.generators.items;

import stonering.entity.Aspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.enums.items.TagEnum;
import stonering.entity.item.aspects.FallingAspect;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.entity.item.Item;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.*;

/**
 * Generates item.
 * When player orders some item to produce, he specifies
 * materials for part of this item (through itemSelectors).
 *
 * @author Alexander Kuzyakov on 26.01.2018.
 */
public class ItemGenerator {
    private ItemTypeMap itemTypeMap;
    private MaterialMap materialMap;
    private Map<String, List<String>> defaultAspects;

    public ItemGenerator() {
        itemTypeMap = ItemTypeMap.getInstance();
        materialMap = MaterialMap.instance();
        defaultAspects = new HashMap<>();
        defaultAspects.put("falling", Arrays.asList("1"));
    }

    /**
     * MVP method for creating item.
     */
    public Item generateItem(String name, int materialId, Position position) {
        ItemType type = itemTypeMap.getItemType(name);
        if (type == null) return null;
        Item item = new Item(position, type);
        Material material = MaterialMap.instance().getMaterial(materialId);
        item.setMaterial(materialId);
        for (String tag : material.getTags()) {
            item.tags.add(TagEnum.get(tag));
        }
        generateItemAspects(item);
        return item;
    }

    public Item generateItem(String name, String material, Position position) {
        return generateItem(name, materialMap.getId(material), position);
    }

    /**
     * Seeds have single {@link ItemType} for all plants species.
     */
    public Item generateSeedItem(String specimen, Position position) {
        Item item = new Item(position, itemTypeMap.getItemType("seed"));
        item.setTitle(specimen.substring(0,1).toUpperCase() + specimen.substring(1).toLowerCase() + " seed");
        item.addAspect(new SeedAspect(item, specimen));
        return item;
    }

    /**
     * Generates item by {@link ItemOrder} formed in workbench.
     * TODO fetch item part orders and create corresponding item parts
     */
    public Item generateItemByOrder(Position position, ItemOrder order) {
        Logger.ITEMS.logWarn("Generating creafted item " + order.recipe.itemName + " for " + order.recipe.title);
        return new Item(position, ItemTypeMap.getInstance().getItemType("sickle"));
    }

    /**
     * Creates item aspects by map of aspects names and arguments from {@link ItemType} and default aspects.
     */
    private void generateItemAspects(Item item) {
        ItemType type = item.getType();
        for (String aspectName : defaultAspects.keySet()) {
            if (!type.aspects.containsKey(aspectName))
                item.addAspect(createItemAspect(aspectName, defaultAspects.get(aspectName)));
        }
        for (String aspectName : type.aspects.keySet()) {
            item.addAspect(createItemAspect(aspectName, type.aspects.get(aspectName)));
        }
    }

    /**
     * Creates single aspect of item. All possible aspects should be listed here.
     */
    private Aspect createItemAspect(String name, List<String> params) {
        switch (name) {
            case FallingAspect.NAME:
                return new FallingAspect(null);
            case SeedAspect.NAME:
                return new SeedAspect(null, params.get(0));
            case ItemContainerAspect.NAME:
                return new ItemContainerAspect(null, params.get(0).split("/"));
            default:
                return null;
        }
    }
//TODO non-MVP feature
//    /**
//     * Normal creation of item (order from workbench).
//     *
//     * @param order     order specified by player
//     * @param resources item, carried to workbench
//     * @return
//     */
//    public Item generateItemByOrder(ItemOrder order, List<Item> resources) throws InvalidCraftingOrder {
//        if(validateOrder(order)) {
//            Item item = createItem(order.getType());
//            order.getSelectors().forEach((partTitle, selector) -> item.getParts().put(partTitle, createItemPart(order.getType(), partTitle, selector, resources)));
//            return item;
//        } else {
//            throw new InvalidCraftingOrder(order);

//        }
//    }
//    /**
//     * Creates item part to be added to item.
//     *
//     * @param itemType
//     * @param partTitle
//     * @param selector
//     * @param resources
//     * @return
//     */
//    private ItemPart createItemPart(ItemType itemType, String partTitle, ItemSelector selector, List<Item> resources) {
//        List<Item> item = selector.selectItems(resources); // item to spend

//        return new ItemPart(partTitle, item.get(0).getMainMaterial(), selectStep(itemType, partTitle).getVolume());

//    }

    /**
     * Checks if all required steps from item type persist in the order.
     */
    private boolean validateOrder(ItemOrder order) {
//        for (ItemPartType step : order.getType().getParts()) {
//            if(!step.isOptional() && !order.getSelectors().containsKey(step.getTitle())) { // required step missed in order.
//                return false;
//            }
//        }
        return true;
    }
    //TODO add itemName everywhere
//    /**
//     * Generates item with default materials of parts.
//     *
//     * @param itemName
//     * @param mainMaterial material of main item part. See parts in {@link ItemType} -1 if unspecified.
//     * @return
//     */
//    public Item generateMockItem(String itemName, int mainMaterial) throws FaultDescriptionException {
//        ItemType itemType = itemTypeMap.getItemType(itemName);
//        Item item = new Item(null, itemType);
//        for (ItemPartType step : itemType.getParts()) {
//            item.getParts().put(step.getTitle(), createMockItemPart(step, itemName));
//        }
//        if(mainMaterial >= 0) {
//            item.getMainPart_().setMaterial(mainMaterial);
//        }

//        return item;

//    }
}
