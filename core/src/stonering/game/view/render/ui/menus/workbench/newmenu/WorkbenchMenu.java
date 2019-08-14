package stonering.game.view.render.ui.menus.workbench.newmenu;

import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.game.view.render.ui.menus.workbench.newmenu.orderlist.OrderList;
import stonering.game.view.render.ui.menus.workbench.newmenu.recipelist.RecipeList;

/**
 * Contains three sections: {@link RecipeList}, {@link OrderList}, {@link OrderDetails}
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu {
    private RecipeList recipeList;
    private OrderList orderList;

    public WorkbenchMenu(Building workbench) {
        recipeList = new RecipeList(workbench.getAspect(WorkbenchAspect.class));
        orderList = new OrderList(workbench.getAspect(WorkbenchAspect.class));

    }
}
