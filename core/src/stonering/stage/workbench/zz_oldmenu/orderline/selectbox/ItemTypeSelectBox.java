package stonering.stage.workbench.zz_oldmenu.orderline.selectbox;

import stonering.entity.crafting.IngredientOrder;
import stonering.enums.items.recipe.Ingredient;
import stonering.stage.workbench.zz_oldmenu.orderline.ItemPartSelection;

/**
 * Select box for selecting item type for {@link Ingredient}
 *
 * @author Alexander on 26.06.2019.
 */
public class ItemTypeSelectBox extends OrderLineSelectBox {

    public ItemTypeSelectBox(IngredientOrder ingredientOrder, ItemPartSelection selection) {
        super(ingredientOrder, selection);
        setItems(ingredientOrder.ingredient.itemTypes);
        setSelectedIndex(0);
    }

    @Override
    protected void handleSelection() {
//        ingredientOrder.setSelectedItemType(getSelected());
        super.handleSelection();
    }
}
