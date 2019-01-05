package stonering.generators.items;

import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.crafting.ItemPartType;
import stonering.entity.local.items.ItemPart;
import stonering.entity.local.items.aspects.FallingAspect;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.FaultDescriptionException;
import stonering.generators.aspect.AspectGenerator;
import stonering.entity.local.items.Item;
import stonering.util.global.TagLoggersEnum;

import java.util.Set;

/**
 * Generates items.
 * When player orders some item to produce, he specifies
 * materials for part of this item (through itemSelectors).
 *
 * @author Alexander Kuzyakov on 26.01.2018.
 */
public class ItemGenerator {
    private ItemTypeMap itemTypeMap;
    private MaterialMap materialMap;

    public ItemGenerator() {
        init();
    }

    private void init() {
        itemTypeMap = ItemTypeMap.getInstance();
        materialMap = MaterialMap.getInstance();
    }

    /**
     * MVP method for creating items.
     *
     * @param name
     * @param material
     * @return
     */
    public Item generateItem(String name, int material) {
        Item item = new Item(null, itemTypeMap.getItemType(name));
        item.setMaterial(material);
        item.getAspects().put("falling", new FallingAspect(item));
        return item;
    }

    public Item generateItem(String name, String material) {
        Item item = new Item(null, itemTypeMap.getItemType(name));
        item.setMaterial(materialMap.getId(material));
        item.getAspects().put("falling", new FallingAspect(item));
        return item;
    }

//TODO non-MVP feature
//    /**
//     * Normal creation of item (order from workbench).
//     *
//     * @param order     order specified by player
//     * @param resources items, carried to workbench
//     * @return
//     */
//    public Item generateItem(ItemOrder order, List<Item> resources) throws InvalidCraftinOrder {
//        if(validateOrder(order)) {
//            Item item = createItem(order.getType());
//            order.getSelectors().forEach((partTitle, selector) -> item.getParts().put(partTitle, createItemPart(order.getType(), partTitle, selector, resources)));
//            return item;
//        } else {
//            throw new InvalidCraftinOrder(order);
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
//        List<Item> items = selector.selectItems(resources); // items to spend
//        return new ItemPart(partTitle, items.get(0).getMainMaterial(), selectStep(itemType, partTitle).getVolume());
//    }

    /**
     * Selects item crafting step by title.
     *
     * @param type
     * @param title
     * @return
     */
    private ItemPartType selectStep(ItemType type, String title) {
        for (ItemPartType step : type.getParts()) {
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
        itemType.getAspects().keySet().forEach(aspectName -> AspectGenerator.createAspect(aspectName, item).ifPresent(item::addAspect));
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
        String materiaType = step.getVariants().get(0).getMaterial();
        Set<Integer> materials = materialMap.getMaterialsByType(materiaType);
        if(materials.isEmpty()) throw new FaultDescriptionException("Material type " + materiaType + " for item " + itemName + " is invalid");
        return new ItemPart(step.getTitle(), materials.iterator().next(), 10); //TODO
    }

    public Item generateItem(ItemOrder order) {
        TagLoggersEnum.ITEMS.logWarn("Generating mock item"); //TODO
        return new Item(null, ItemTypeMap.getInstance().getItemType("sickle"));
    }
}
