package stonering.game.view.render.ui.menus.workbench.newmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.game.view.render.ui.menus.workbench.newmenu.details.OrderDetailsPane;
import stonering.game.view.render.ui.menus.workbench.newmenu.orderlist.OrderList;
import stonering.game.view.render.ui.menus.workbench.newmenu.recipelist.RecipeList;
import stonering.util.global.StaticSkin;

import java.awt.*;

/**
 * Contains three sections: {@link RecipeList}, {@link OrderList}, {@link OrderDetailsPane}
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu extends Window {
    private RecipeList recipeList;
    private OrderList orderList;
    private OrderDetailsPane orderDetailsPane;

    public WorkbenchMenu(Building workbench) {
        super(workbench.getType().title, StaticSkin.getSkin());
        recipeList = new RecipeList(workbench.getAspect(WorkbenchAspect.class));
        orderList = new OrderList(workbench.getAspect(WorkbenchAspect.class));
        orderDetailsPane = new OrderDetailsPane();
    }
}
