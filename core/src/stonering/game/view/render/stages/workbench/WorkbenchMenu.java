package stonering.game.view.render.stages.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.game.view.render.stages.workbench.details.OrderDetailsSection;
import stonering.game.view.render.stages.workbench.orderlist.OrderListSection;
import stonering.game.view.render.stages.workbench.recipelist.RecipeListSection;
import stonering.util.global.StaticSkin;

/**
 * Contains three sections: {@link RecipeListSection}, {@link OrderListSection}, {@link OrderDetailsSection}.
 *
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu extends Window {
    public final RecipeListSection recipeListSection;
    public final OrderListSection orderListSection;
    public final OrderDetailsSection orderDetailsSection;

    public WorkbenchMenu(Building workbench) {
        super(workbench.getType().title, StaticSkin.getSkin());
        defaults().size(300, 700).pad(10);
        add(recipeListSection = new RecipeListSection(workbench.getAspect(WorkbenchAspect.class), this));
        add(orderListSection = new OrderListSection(workbench.getAspect(WorkbenchAspect.class)));
        add(orderDetailsSection = new OrderDetailsSection());
    }

    public void initFocus() {
        getStage().setKeyboardFocus(recipeListSection);
    }
}
