package stonering.stage.workbench.zz_oldmenu.orderline;

import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.stage.workbench.zz_oldmenu.WorkbenchMenuq;
import stonering.stage.workbench.zz_oldmenu.orderline.selectbox.RecipeSelectBox;

/**
 * This line shows select box with list of available crafting recipes.
 * When recipe is selected, this line replaces itself with {@link ItemCraftingOrderLine}
 *
 * @author Alexander_Kuzyakov on 24.06.2019.
 */
public class EmptyOrderLine extends OrderLine {
    private static final String BACKGROUND_NAME = "workbench_order_line";
    private static final String LINE_HINT = "WS: select recipe, ED: confirm";
    private RecipeSelectBox selectBox;

    public EmptyOrderLine(WorkbenchMenuq menu) {
        super(menu, LINE_HINT);
        createSelectBox();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(getStage().getKeyboardFocus() == selectBox);
    }

    private void createSelectBox() {
        leftHG.addActor(selectBox = new RecipeSelectBox(menu.getWorkbenchAspect().getRecipes()));
        selectBox.setSelectListener(e -> {
            e.stop();
            if (selectBox.getList().getStage() != null) { // list is shown
                selectBox.hideList();
                if (!selectBox.getSelected().equals(selectBox.getPlaceholder())) { // placeholder is selected
                    replaceSelfWith(selectBox.getSelected());
                } else { // not a valid case
                    warningLabel.setText("Item not selected");
                }
            } else {  // open list
                selectBox.showList();
            }
            return true;
        });
        selectBox.setCancelListener(e -> {
            e.stop();
            hide();
            return true;
        });
    }

    private void replaceSelfWith(Recipe recipe) {
        hide();
        ItemOrder order = new ItemOrder(recipe);
        GameMvc.instance().getModel().get(BuildingContainer.class).workbenchSystem.addOrder(menu.getWorkbenchAspect(), order);
        ItemCraftingOrderLine orderLine = new ItemCraftingOrderLine(menu, order);
        orderLine.show();
        orderLine.navigateToFirst();
    }

    @Override
    public void show() {
        super.show();
        getStage().setKeyboardFocus(selectBox);
    }
}
