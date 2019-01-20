package stonering.entity.local.crafting;

import stonering.entity.local.items.selectors.ItemSelector;
import stonering.game.core.GameMvc;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * @author Alexander on 05.01.2019.
 */
public class ItemPartOrder {
    private GameMvc gameMvc;
    private ItemOrder order;
    private String name;                     // ItemPart name
    private List<ItemSelector> itemSelectors;
    private ItemSelector selected;

    public ItemPartOrder(GameMvc gameMvc, ItemOrder order, String name) {
        this.gameMvc = gameMvc;
        this.order = order;
        this.name = name;
    }

    public void refreshSelectors(Position workbenchPosition) {
        itemSelectors = gameMvc.getModel().getItemContainer().getItemSelectorsForItemPartRecipe(order.getRecipe().getItemPartRecipe(name), workbenchPosition);

    }

    /**
     * Checks if selected item selector is present and similar to one of item selectors.
     */
    public boolean isSelectedPossible() {
        if (selected == null) return true;                 // no items selected for this step
        for (ItemSelector itemSelector : itemSelectors) {
            if(itemSelector.equals(selected)) return true;
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
