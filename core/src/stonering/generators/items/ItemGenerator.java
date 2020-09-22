package stonering.generators.items;

import stonering.entity.Aspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.ItemPart;
import stonering.entity.item.aspects.FoodItemAspect;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.RenderAspect;
import stonering.entity.item.aspects.FallingAspect;
import stonering.enums.items.FoodCategoryEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.entity.material.Material;
import stonering.enums.materials.MaterialMap;
import stonering.entity.item.Item;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.*;

/**
 * Generates item. Fills item aspects.
 *
 * @author Alexander Kuzyakov on 26.01.2018.
 */
public class ItemGenerator {
    private Map<String, List<String>> defaultAspects = new HashMap<>();

    public ItemGenerator() {
        defaultAspects.put("falling", Collections.singletonList("1")); // most items fall down
    }

    /**
     * Creates item of given type, setting given material to main part. Other parts get default material, and should be changed after creation.
     */
    public Item generateItem(String name, int materialId, Position position) {
        ItemType type = ItemTypeMap.getItemType(name);
        if (type == null) return null;
        Item item = new Item(position, type);
        type.requiredParts.forEach(part -> item.parts.put(part, new ItemPart(item, part, materialId)));
        Material material = MaterialMap.getMaterial(materialId);
        item.material = materialId;
        item.tags.addAll(material.tags);
        generateItemAspects(item);
        updateItemTitle(item);
        return item;
    }

    public Item generateItem(String name, String material, Position position) {
        return generateItem(name, MaterialMap.getId(material), position);
    }

    /**
     * Generates item by {@link ItemOrder} formed in workbench.
     * Item parts are created with materials of ingredient items.
     * If recipe specifies another material, main part of item is set to that material.
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
            item = new Item(null, ItemTypeMap.getItemType(order.recipe.newType));
        }
        order.ingredientOrders.values().stream()
                .filter(ingredientOrder -> !ingredientOrder.ingredient.key.equals("consume"))
                .filter(ingredientOrder -> !ingredientOrder.ingredient.key.equals("main"))
                .map(ingredientOrder -> createItemPart(ingredientOrder, item))
                .filter(Objects::nonNull)
                .forEach(itemPart -> item.parts.put(itemPart.name, itemPart));
        Optional.ofNullable(order.recipe.newTag)
                .ifPresent(tag -> {
                    item.tags.add(tag);
                    if(tag.onAdd != null) tag.onAdd.accept(item);
                }); // add tag
        Optional.ofNullable(order.recipe.removeTag).ifPresent(item.tags::remove); // add tag
        generateItemAspects(item);
        setItemMaterialByOrder(item, order);
        updateItemTitle(item);
        return item;
    }

    private void setItemMaterialByOrder(Item item, ItemOrder order) {
        if (!order.ingredientOrders.containsKey("main")) {
            ItemPart mainPart = item.parts.get(item.type.requiredParts.get(0));
            item.material = mainPart.material; // get material of main part
            Optional.ofNullable(order.recipe.newMaterial).ifPresent(materialString -> { // use recipe material
                if (materialString.startsWith("_")) { // use reaction material
                    String[] args = materialString.split(":");
                    materialString = MaterialMap.getMaterial(item.parts.get(args[1]).material).reactions.get(args[0]).get(0);
                }
                mainPart.material = MaterialMap.getId(materialString);
                item.material = mainPart.material;
            });
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
        ItemType type = item.type;
        item.add(new RenderAspect(AtlasesEnum.items.getBlockTile(item.type.atlasName, item.type.atlasXY[0], item.type.atlasXY[1])));
        defaultAspects.entrySet().stream()
                .map(entry -> createItemAspect(entry.getKey(), entry.getValue()))
                .forEach(item::add);
        for (String aspectName : defaultAspects.keySet()) {
            if (!type.itemAspects.containsKey(aspectName))
                item.add(createItemAspect(aspectName, defaultAspects.get(aspectName)));
        }
        for (String aspectName : type.itemAspects.keySet()) {
            item.add(createItemAspect(aspectName, type.itemAspects.get(aspectName)));
        }
    }

    private void updateItemTitle(Item item) {
        StringBuilder builder = new StringBuilder();
        item.tags.stream()
                .filter(ItemTagEnum::isDisplayable)
                .map(tag -> tag.toString() + " ")
                .forEach(builder::append);
        builder.append(MaterialMap.getMaterial(item.material).name).append(" ").append(item.type.title);
        item.title = capitalize(builder.toString());
    }

    private String capitalize(String text) {
        return (text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase()).replace('_', ' ');
    }

    /**
     * Creates single aspect of item. All possible aspects should be listed here.
     */
    private Aspect createItemAspect(String name, List<String> params) {
        switch (name) {
            case "falling":
                return new FallingAspect(null);
            case "seed":
                return new SeedAspect(params.get(0));
            case "item_container":
                return new ItemContainerAspect(null);
            case "food":
                // TODO set food type by tags
                return new FoodItemAspect(Integer.parseInt(params.get(0)), FoodCategoryEnum.READY_TO_EAT);
            default:
                return null;
        }
    }
}
