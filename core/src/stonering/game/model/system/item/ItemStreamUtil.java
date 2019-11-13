package stonering.game.model.system.item;

import stonering.entity.crafting.BuildingComponent;
import stonering.entity.crafting.BuildingComponentVariant;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.TagEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains streams util methods for faster lookup of items from {@link ItemContainer}
 *
 * @author Alexander on 11.10.2019.
 */
public class ItemStreamUtil {
    private final ItemContainer container;

    public ItemStreamUtil(ItemContainer container) {
        this.container = container;
    }

    /**
     * Gets all materials for all variants of crafting step. Used for filling materialSelectList.
     */
    public List<Item> getAvailableMaterialsForBuildingStep(BuildingComponent step, Position position) {
        List<Item> items = new ArrayList<>();
        for (BuildingComponentVariant variant : step.componentVariants) {
            items.addAll(new ItemsStream(container.entities)
                    .filterByTag(variant.tag)
                    .filterByType(variant.itemType)
                    .filterByReachability(position)
                    .toList());
        }
        return items;
    }

    /**
     * Returns list of items from map, that can be used for given recipe.
     */
    public List<Item> getItemsForIngredient(Ingredient ingredient, Position position) {
        return new ItemsStream(container.entities)
                .filterByTag(ingredient.tag)
                .filterByTypes(ingredient.itemTypes)
                .filterByReachability(position)
                .toList();
    }

    public Item getItemAvailableBySelector(ItemSelector itemSelector, Position position) {
        return new ItemsStream(itemSelector.selectItems(container.entities))
                .filterByReachability(position)
                .getNearestTo(position);
    }

    public List<Item> getItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        return new ItemsStream(itemSelector.selectItems(container.entities))
                .filterByReachability(position)
                .toList();
    }

    public List<Item> getNearestItems(List<Item> items, Position target, int number) {
        return new ItemsStream(items).getNearestTo(target, number)
                .toList();
    }

    public Item getNearestItemWithTag(Position position, TagEnum tag) {
        return new ItemsStream(container.entities)
                .filterByTag(tag)
                .getNearestTo(position);
    }

    public boolean itemIsAvailable(Item item, Position position) {
        //TODO check containers
        return item.position != null &&
                GameMvc.instance().getModel().get(LocalMap.class).passage.positionReachable(position, item.position, false);
    }
}
