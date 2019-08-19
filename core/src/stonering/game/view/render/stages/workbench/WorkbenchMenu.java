package stonering.game.view.render.stages.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.game.view.render.stages.workbench.details.OrderDetailsPane;
import stonering.game.view.render.stages.workbench.orderlist.OrderList;
import stonering.game.view.render.stages.workbench.recipelist.RecipeListSection;
import stonering.util.global.StaticSkin;

/**
 * Contains three sections: {@link RecipeListSection}, {@link OrderList}, {@link OrderDetailsPane}.
 *
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu extends Window {
    private RecipeListSection recipeListSection;
    private OrderList orderList;
    private OrderDetailsPane orderDetailsPane;

    public WorkbenchMenu(Building workbench) {
        super(workbench.getType().title, StaticSkin.getSkin());
        defaults().size(300, 700).pad(10);
        add(recipeListSection = new RecipeListSection(workbench.getAspect(WorkbenchAspect.class)));
        add(orderList = new OrderList(workbench.getAspect(WorkbenchAspect.class)));
        add(orderDetailsPane = new OrderDetailsPane());
    }

    public void initFocus() {
        getStage().setKeyboardFocus(recipeListSection);
    }


}
