package stonering.stage.workbench.zz_oldmenu.orderline.selectbox;

import stonering.entity.crafting.IngredientOrder;
import stonering.stage.workbench.zz_oldmenu.orderline.ItemPartSelection;

/**
 * Select box for crafting UI to select materials for item parts.
 * By default, 'any' item selector is selected.
 * Player can specify specific materials by opening select box and selecting another option.
 *
 * @author Alexander on 25.06.2019.
 */
public class MaterialSelectBox extends OrderLineSelectBox {

    public MaterialSelectBox(IngredientOrder ingredientOrder, ItemPartSelection selection) {
        super(ingredientOrder, selection);
//        setItems(ingredientOrder.ingredient.getPossibleMaterials());
        setSelectedIndex(0);
    }

    @Override
    protected void handleSelection() {
//        ingredientOrder.setSelectedMaterial(getSelected());
        super.handleSelection();
    }
}
