package stonering.game.view.render.ui.menus.workbench.orderline.selectbox;

import stonering.entity.local.crafting.ItemPartOrder;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.game.view.render.ui.menus.workbench.orderline.ItemPartSelection;

/**
 * Select box for selecting item type for {@link ItemPartRecipe}
 *
 * @author Alexander on 26.06.2019.
 */
public class ItemTypeSelectBox extends OrderLineSelectBox {

    public ItemTypeSelectBox(ItemPartOrder itemPartOrder, ItemPartSelection selection) {
        super(itemPartOrder, selection);
        setItems(itemPartOrder.getItemPartRecipe().itemTypes);
        setSelectedIndex(0);
    }

    @Override
    protected void handleSelection() {
        itemPartOrder.setSelectedItemType(getSelected());
        super.handleSelection();
    }
}
