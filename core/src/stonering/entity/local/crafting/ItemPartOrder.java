package stonering.entity.local.crafting;

import stonering.entity.local.item.selectors.AnyMaterialTagItemSelector;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Part of {@link ItemOrder}.
 *
 * @author Alexander on 05.01.2019.
 */
public class ItemPartOrder {
    private ItemOrder order;
    private String name;
    private List<ItemSelector> itemSelectors;
    private ItemSelector selected;
    private ItemSelector anyMaterialSelector;

    public ItemPartOrder(ItemOrder order, String name) {
        this.order = order;
        this.name = name;
        itemSelectors = new ArrayList<>();
        anyMaterialSelector = new AnyMaterialTagItemSelector(order.getRecipe().getItemPartRecipe(name));
    }

    /**
     * Updates selectors, to have only those which have items available from selected position.
     */
    public void refreshSelectors(Position workbenchPosition) {
        ItemPartRecipe partRecipe = order.getRecipe().getItemPartRecipe(name);
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        itemSelectors = container.getItemSelectorsForItemPartRecipe(partRecipe, workbenchPosition);
    }

    /**
     * Checks if selected item selector is present and similar to one of item selectors.
     */
    public boolean isSelectedPossible() {
        if (selected == null) return true;                 // no item selected for this step
        for (ItemSelector itemSelector : itemSelectors) {
            if (itemSelector.equals(selected)) return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemSelector> getItemSelectors() {
        return itemSelectors;
    }

    public ItemSelector getSelected() {
        return selected;
    }

    public void setSelected(ItemSelector selected) {
        this.selected = selected;
    }

    public ItemOrder getOrder() {
        return order;
    }

    public ItemSelector getAnyMaterialSelector() {
        return anyMaterialSelector;
    }
}
