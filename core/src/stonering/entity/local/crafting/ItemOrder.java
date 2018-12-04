package stonering.entity.local.crafting;

import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.utils.global.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains recipe, item name and material id for crafting item.
 * Task for crafting creation is based on this.
 *
 * For each required item part, player should specify item selector.
 *
 * Changing order properties during task execution will not update task.
 * Repeated task will update on next iteration.
 *
 * mvp is crafting 1 item from 1 item/material.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemOrder {
    private GameMvc gameMvc;
    private Recipe recipe;
    private HashMap<String, ItemSelector> selectors;                            // itemPart to items selected for variant.

    private Map<String, Pair<String, String>> materialItemMap;                  // ui string to (item type, material name)
    private String selectedString;

    public ItemOrder(GameMvc gameMvc, Recipe recipe) {
        this.gameMvc = gameMvc;
        this.recipe = recipe;
        materialItemMap = new HashMap<>();
        selectors = new HashMap<>();
    }

    /**
     * Mvp for selecting material item. Gets items from map, filters them by availability and groups by materials and types.
     *
     * //TODO add steps usage.
     * @return
     */
    public Set<String> getAvailableItemList(Position position) {
        ItemContainer itemContainer = gameMvc.getModel().getItemContainer();
        List<Item> items = itemContainer.getResourceItemListByMaterialType(recipe.getMaterial());
        items = itemContainer.filterUnreachable(items, position);
        materialItemMap = itemContainer.groupItemsByTypesAndMaterials(items);
        return materialItemMap.keySet();
    }

    /**
     * Updates one selector. Used when payer selects some items for crafting.
     *
     * @param part
     * @param selector
     */
    public void setSelector(String part, ItemSelector selector) {
        selectors.put(part, selector);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public HashMap<String, ItemSelector> getSelectors() {
        return selectors;
    }

    public String getSelectedString() {
        return selectedString;
    }

    public void setSelectedString(String selectedString) {
        this.selectedString = selectedString;
    }
}
