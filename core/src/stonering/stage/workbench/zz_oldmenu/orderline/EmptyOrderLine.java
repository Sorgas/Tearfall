package stonering.stage.workbench.zz_oldmenu.orderline;

import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.stage.workbench.zz_oldmenu.WorkbenchMenuq;

/**
 * This line shows select box with list of available crafting recipes.
 * When recipe is selected, this line replaces itself with {@link ItemCraftingOrderLine}
 *
 * @author Alexander_Kuzyakov on 24.06.2019.
 */
public class EmptyOrderLine extends OrderLine {
    private static final String BACKGROUND_NAME = "workbench_order_line";
    private static final String LINE_HINT = "WS: select recipe, ED: confirm";

    public EmptyOrderLine(WorkbenchMenuq menu) {
        super(menu, LINE_HINT);
        createSelectBox();
    }

    private void createSelectBox() {
    }

    private void replaceSelfWith(Recipe recipe) {
        hide();
        ItemOrder order = new ItemOrder(recipe);
        GameMvc.model().get(BuildingContainer.class).workbenchSystem.addOrder(menu.getWorkbenchAspect(), order);
        ItemCraftingOrderLine orderLine = new ItemCraftingOrderLine(menu, order);
        orderLine.show();
        orderLine.navigateToFirst();
    }
}
