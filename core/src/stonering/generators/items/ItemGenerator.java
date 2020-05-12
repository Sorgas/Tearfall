
package stonering.generators.items;

import stonering.entity.Aspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.RenderAspect;
import stonering.entity.item.aspects.FallingAspect;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.entity.item.Item;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.*;

/**
 * Generates item. Fills item aspects.
 *
 * @author Alexander Kuzyakov on 26.01.2018.
 */
public class ItemGenerator {
    private ItemTypeMap itemTypeMap;
    private MaterialMap materialMap;
    private Map<String, List<String>> defaultAspects;
    private final int DEFAULT_MATERIAL;

    public ItemGenerator() {
        itemTypeMap = ItemTypeMap.instance();
        materialMap = MaterialMap.instance();
        defaultAspects = new HashMap<>();
        defaultAspects.put("falling", Arrays.asList("1"));
        DEFAULT_MATERIAL = MaterialMap.instance().getId("iron");
    }

    /**
     * Creates item of given type, setting given material to main part. Other parts get default material, and should be changed after creation.
     */
    public Item generateItem(String name, int materialId, Position position) {
        ItemType type = itemTypeMap.getItemType(name);
        if (type == null) return null;
        Item item = new Item(position, type);
//        item.tags.addAll(type.tags);
        // create all item parts with default material
//        type.parts.forEach(part -> item.parts.put(part.title, new ItemPart(part.title, DEFAULT_MATERIAL)));
//        item.mainPart = new ItemPart(type.parts.isEmpty() ? type.title : type.parts.get(0).title, materialId); // create main part with specified material
        Material material = MaterialMap.instance().getMaterial(materialId);
        item.material = materialId;
        item.tags.addAll(material.tags);
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
        item.title = specimen.substring(0, 1).toUpperCase() + specimen.substring(1).toLowerCase() + " seed";
        item.material = MaterialMap.instance().getId("generic_plant");
        generateItemAspects(item);
        item.add(new SeedAspect(item));
        item.get(SeedAspect.class).specimen = specimen;
        return item;
    }

    /**
     * Generates item by {@link ItemOrder} formed in workbench.
     * TODO fetch item part orders and create corresponding item parts
     */
    public Item generateItemByOrder(ItemOrder order) {
        Logger.ITEMS.logDebug("Generating crafted item " + order.recipe.itemName + " for " + order.recipe.title);
        Item item = null;
        switch (order.recipe.type) {
            case COMBINE: // new item is created with parts, specified in recipe.
                item = new Item(null, ItemTypeMap.instance().getItemType(order.recipe.itemName));
                break;
            case TRANSFORM: // new parts from recipe are added to existing main item

                item = order.main.items.iterator().next();
        }
        item.tags.add(order.recipe.newTag);
        for (String key : order.parts.keySet()) {
            //TODO add part(non MVP)
        }
        if (order.recipe.newTag != null) item.tags.add(order.recipe.newTag);
        generateItemAspects(item);
        return item;
    }

    /**
     * Creates item aspects by map of aspects names and arguments from {@link ItemType} and default aspects.
     */
    private void generateItemAspects(Item item) {
        ItemType type = item.getType();
        item.add(new RenderAspect(item, item.type.atlasXY, AtlasesEnum.items));
        for (String aspectName : defaultAspects.keySet()) {
            if (!type.itemAspects.containsKey(aspectName))
                item.add(createItemAspect(aspectName, defaultAspects.get(aspectName)));
        }
        for (String aspectName : type.itemAspects.keySet()) {
            item.add(createItemAspect(aspectName, type.itemAspects.get(aspectName)));
        }
    }

    /**
     * Creates single aspect of item. All possible aspects should be listed here.
     */
    private Aspect createItemAspect(String name, List<String> params) {
        switch (name) {
            case "falling":
                return new FallingAspect(null);
            case "seed":
                return new SeedAspect(null);
            case "item_container":
                return new ItemContainerAspect(null, params.get(0).split("/"));
            default:
                return null;
        }
    }
}
