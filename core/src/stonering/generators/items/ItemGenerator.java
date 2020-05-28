
package stonering.generators.items;

import stonering.entity.Aspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.ItemPart;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.RenderAspect;
import stonering.entity.item.aspects.FallingAspect;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.entity.material.Material;
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
    private Map<String, List<String>> defaultAspects;

    public ItemGenerator() {
        itemTypeMap = ItemTypeMap.instance();
        defaultAspects = new HashMap<>();
        defaultAspects.put("falling", Arrays.asList("1")); // most items fall down
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
        Material material = MaterialMap.getMaterial(materialId);
        item.material = materialId;
        item.tags.addAll(material.tags);
        generateItemAspects(item);
        return item;
    }

    public Item generateItem(String name, String material, Position position) {
        return generateItem(name, MaterialMap.getId(material), position);
    }

    /**
     * Seeds have single {@link ItemType} for all plants species.
     */
    public Item generateSeedItem(String specimen, Position position) {
        Item item = new Item(position, itemTypeMap.getItemType("seed"));
        item.title = specimen.substring(0, 1).toUpperCase() + specimen.substring(1).toLowerCase() + " seed";
        item.material = MaterialMap.getId("generic_plant");
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
        Logger.ITEMS.logDebug("Generating crafted item " + order.recipe.newType + " for " + order.recipe.title);
        IngredientOrder mainIngredient = order.ingredientOrders.get("main");
        Item item; // item will be updated with new parts
        // new item of specified type
        if (mainIngredient != null) {
            item = mainIngredient.items.stream().findFirst().get();
        } else {
            item = new Item(null, ItemTypeMap.instance().getItemType(order.recipe.newType));
        }
        order.ingredientOrders.values().stream()
                .filter(ingredientOrder -> !ingredientOrder.ingredient.key.equals("consume"))
                .filter(ingredientOrder -> !ingredientOrder.ingredient.key.equals("main"))
                .map(ingredientOrder -> createItemPart(ingredientOrder, item))
                .filter(Objects::nonNull)
                .forEach(itemPart -> item.parts.put(itemPart.name, itemPart));
        Optional.ofNullable(order.recipe.newTag).ifPresent(item.tags::add); // add tag
        generateItemAspects(item);
        setItemMaterial(item, order);
        return item;
    }

    private void setItemMaterial(Item item, ItemOrder order) {
        if (!order.ingredientOrders.containsKey("main")) {
            item.material = item.parts.get(item.type.name).material;
            String materialString = order.recipe.newMaterial;
            if(materialString != null) { // use material specified in recipe
                if(materialString.startsWith("_")) { // use reaction material
                    String[] args = materialString.split(":");
                    materialString = (String) MaterialMap.getMaterial(item.parts.get(args[0]).material).reactions.get(args[0]).get(0);
                }
                item.material = MaterialMap.getId(materialString);
            }
        }
    }

    private ItemPart createItemPart(IngredientOrder ingredientOrder, Item item) {
        Item materialItem = ingredientOrder.items.stream().findFirst().orElse(null);
        return materialItem != null
                ? new ItemPart(item, ingredientOrder.ingredient.key, materialItem.material)
                : Logger.ITEMS.logError("ingredient order " + ingredientOrder.ingredient.key + " for item " + item + " has no items set.", null);
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
