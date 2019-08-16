package stonering.game.view.render.stages.workbench.newmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.game.view.render.stages.workbench.newmenu.details.OrderDetailsPane;
import stonering.game.view.render.stages.workbench.newmenu.orderlist.OrderList;
import stonering.game.view.render.stages.workbench.newmenu.recipelist.RecipeList;
import stonering.util.global.StaticSkin;

/**
 * Contains three sections: {@link RecipeList}, {@link OrderList}, {@link OrderDetailsPane}.
 *
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu extends Window {
    private RecipeList recipeList;
    private OrderList orderList;
    private OrderDetailsPane orderDetailsPane;

    public WorkbenchMenu(Building workbench) {
        super(workbench.getType().title, StaticSkin.getSkin());
        add(recipeList = new RecipeList(workbench.getAspect(WorkbenchAspect.class)));
        add(orderList = new OrderList(workbench.getAspect(WorkbenchAspect.class)));
        add(orderDetailsPane = new OrderDetailsPane());
    }
}
