package stonering.generators.items;

import stonering.entity.local.Aspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.item.aspects.SeedAspect;
import stonering.enums.items.type.ItemPartType;
import stonering.entity.local.item.ItemPart;
import stonering.entity.local.item.aspects.FallingAspect;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.FaultDescriptionException;
import stonering.generators.aspect.AspectGenerator;
import stonering.entity.local.item.Item;
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
    private transient MaterialMap materialMap;
    private Map<String, List<String>> defaultAspects;

    public ItemGenerator() {
        itemTypeMap = ItemTypeMap.getInstance();
        materialMap = MaterialMap.getInstance();
        defaultAspects = new HashMap<>();
        defaultAspects.put("falling", Arrays.asList("1"));
    }

    /**
     * MVP method for creating item.
     */
    public Item generateItem(String name, int material) {
        ItemType type = itemTypeMap.getItemType(name);
        if(type == null) return null;
        Item item = new Item(null, type);
        item.setMaterial(material);
        generateItemAspects(item);
        return item;
    }

    public Item generateItem(String name, String material) {
        return generateItem(name, materialMap.getId(material));
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
//    public Item generateItem(ItemOrder order, List<Item> resources) throws InvalidCraftingOrder {
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
     * Selects item crafting step by title.
     *
     * @param type
     * @param title
     * @return
     */
    private ItemPartType selectStep(ItemType type, String title) {
        for (ItemPartType step : type.parts) {
            if (step.getTitle().equals(title)) {
                return step;
            }
        }
        return null;
    }

    /**
     * Creates item without parts.
     *
     * @param itemType
     * @return
     */
    private Item createItem(ItemType itemType) {
        Item item = new Item(null, itemType);
        itemType.aspects.keySet().forEach(aspectName -> AspectGenerator.createAspect(aspectName, item).ifPresent(item::addAspect));
        return item;
    }

    /**
     * Checks if all required steps from item type persist in the order.
     *
     * @param order
     * @return
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

    /**
     * Creates itemPart with material from first variant.
     *
     * @param step
     * @param itemName
     * @return
     * @throws FaultDescriptionException
     */
    private ItemPart createMockItemPart(ItemPartType step, String itemName) throws FaultDescriptionException {
        String tag = step.getComponentVariants().get(0).getTag();
        Set<Integer> materials = materialMap.getMaterialsByType(tag);
        if (materials.isEmpty())
            throw new FaultDescriptionException("Material type " + tag + " for item " + itemName + " is invalid");
        return new ItemPart(step.getTitle(), materials.iterator().next(), 10); //TODO
    }

    public Item generateItem(ItemOrder order) {
        Logger.ITEMS.logWarn("Generating mock item"); //TODO
        return new Item(null, ItemTypeMap.getInstance().getItemType("sickle"));
    }
}
