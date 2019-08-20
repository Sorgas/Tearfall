package stonering.game.view.render.stages.workbench.oldmenu.orderline.selectbox;

import stonering.entity.crafting.ItemPartOrder;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.game.view.render.stages.workbench.oldmenu.orderline.ItemPartSelection;

/**
 * Select box for selecting item type for {@link ItemPartRecipe}
 *
 * @author Alexander on 26.06.2019.
 */
public class ItemTypeSelectBox extends OrderLineSelectBox {

    public ItemTypeSelectBox(ItemPartOrder itemPartOrder, ItemPartSelection selection) {
        super(itemPartOrder, selection);
        setItems(itemPartOrder.partRecipe.itemTypes);
        setSelectedIndex(0);
    }

    @Override
    protected void handleSelection() {
        itemPartOrder.setSelectedItemType(getSelected());
        super.handleSelection();
    }
}
