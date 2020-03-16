package stonering.game.model.system.item;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

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
     * Returns list of items from map, that can be used for given recipe.
     */
    public List<Item> getItemsForIngredient(Ingredient ingredient, Position position) {
        return new ItemsStream(container.objects)
                .filterHasTag(ingredient.tag)
                .filterByTypes(ingredient.itemTypes)
                .filterByReachability(position)
                .toList();
    }

    public Item getItemAvailableBySelector(ItemSelector itemSelector, Position position) {
        return new ItemsStream(itemSelector.selectItems(container.objects))
                .filterByReachability(position)
                .getNearestTo(position);
    }

    public List<Item> getItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        return new ItemsStream(itemSelector.selectItems(container.objects))
                .filterByReachability(position)
                .toList();
    }

    public List<Item> getNearestItems(List<Item> items, Position target, int number) {
        return new ItemsStream(items).getNearestTo(target, number)
                .toList();
    }

    public Item getNearestItemWithTag(Position position, ItemTagEnum tag) {
        return new ItemsStream(container.objects)
                .filterHasTag(tag)
                .getNearestTo(position);
    }

    public boolean itemIsAvailable(Item item, Position position) {
        //TODO check containers
        return item.position != null &&
                GameMvc.model().get(LocalMap.class).passageMap.util.positionReachable(position, item.position, false);
    }

    /**
     * Gets single available item to be crafting ingredient.
     */
    public Item getItemForIngredient(IngredientOrder order, Position position) {
        return new ItemsStream()
                .filterOnMap()
                .filterBySelector(order.itemSelector)
                .filterByReachability(position)
                .getNearestTo(position);
    }
}
