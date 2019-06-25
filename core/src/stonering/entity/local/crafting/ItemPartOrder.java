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
 * @author Alexander on 05.01.2019.
 */
public class ItemPartOrder {
    private ItemOrder order;
    private String name;                     // ItemPart name
    private List<ItemSelector> itemSelectors;
    private ItemSelector selected;

    public ItemPartOrder(ItemOrder order, String name) {
        this.order = order;
        this.name = name;
        itemSelectors = new ArrayList<>();
    }

    public void refreshSelectors(Position workbenchPosition) {
        ItemPartRecipe partRecipe = order.getRecipe().getItemPartRecipe(name);
        itemSelectors = GameMvc.instance().getModel().get(ItemContainer.class)
                .getItemSelectorsForItemPartRecipe(partRecipe, workbenchPosition);
        itemSelectors.add(new AnyMaterialTagItemSelector(partRecipe));
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

    public void setItemSelectors(List<ItemSelector> itemSelectors) {
        this.itemSelectors = itemSelectors;
    }

    public ItemSelector getSelected() {
        return selected;
    }

    public void setSelected(ItemSelector selected) {
        this.selected = selected;
    }
}
