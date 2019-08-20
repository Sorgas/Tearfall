package stonering.game.view.render.stages.workbench.oldmenu.orderline.selectbox;

import stonering.entity.crafting.ItemPartOrder;
import stonering.game.view.render.stages.workbench.oldmenu.orderline.ItemPartSelection;

/**
 * Select box for crafting UI to select materials for item parts.
 * By default, 'any' item selector is selected.
 * Player can specify specific materials by opening select box and selecting another option.
 *
 * @author Alexander on 25.06.2019.
 */
public class MaterialSelectBox extends OrderLineSelectBox {

    public MaterialSelectBox(ItemPartOrder itemPartOrder, ItemPartSelection selection) {
        super(itemPartOrder, selection);
        setItems(itemPartOrder.partRecipe.getPossibleMaterials());
        setSelectedIndex(0);
    }

    @Override
    protected void handleSelection() {
        itemPartOrder.setSelectedMaterial(getSelected());
        super.handleSelection();
    }
}
