
package stonering.generators.items;

import stonering.entity.Aspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.items.TagEnum;
import stonering.entity.item.aspects.FallingAspect;
import stonering.enums.items.recipe.RecipeType;
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

    public ItemGenerator() {
        itemTypeMap = ItemTypeMap.instance();
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
        for (String tag : material.tags) {
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
        generateItemAspects(item);
        item.addAspect(new SeedAspect(item));
        item.getAspect(SeedAspect.class).specimen = specimen;
        return item;
    }

    /**
     * Generates item by {@link ItemOrder} formed in workbench.
     * TODO fetch item part orders and create corresponding item parts
     */
    public Item generateItemByOrder(ItemOrder order) {
        Logger.ITEMS.logDebug("Generating crafted item " + order.recipe.itemName + " for " + order.recipe.title);
        Item item = null;
        switch(order.recipe.type) {
            case COMBINE: // new item is created with parts, specified in recipe.
                item = new Item(null, ItemTypeMap.instance().getItemType(order.recipe.itemName));
                break;
            case TRANSFORM: // new parts from recipe are added to existing main item
                item = order.main.item;
        }
        item.tags.add(order.recipe.newTag);
        for (String key : order.parts.keySet()) {
            //TODO add part
        }
        if(order.recipe.newTag != null) item.tags.add(order.recipe.newTag);
        generateItemAspects(item);
        return item;
    }

    /**
     * Creates item aspects by map of aspects names and arguments from {@link ItemType} and default aspects.
     */
    private void generateItemAspects(Item item) {
        ItemType type = item.getType();
        item.addAspect(new RenderAspect(item, item.type.atlasXY, AtlasesEnum.items));
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
                return new SeedAspect(null);
            case ItemContainerAspect.NAME:
                return new ItemContainerAspect(null, params.get(0).split("/"));
            default:
                return null;
        }
    }
}
