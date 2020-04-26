package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.stage.workbench.details.OrderDetailsSection;
import stonering.stage.workbench.orderlist.OrderListSection;
import stonering.stage.workbench.recipelist.RecipeListSection;
import stonering.widget.util.WrappedLabel;
import stonering.util.global.StaticSkin;

/**
 * Contains three sections: {@link RecipeListSection}, {@link OrderListSection}, {@link OrderDetailsSection}.
 * One section can be focused at a time.
 *
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu extends Table {
    private final int HEADER_HEIGHT = 100;
    public final RecipeListSection recipeListSection;
    public final OrderListSection orderListSection;
    public final OrderDetailsSection orderDetailsSection;
    public final WrappedLabel recipesHeader;
    public final WrappedLabel ordersHeader;
    public final WrappedLabel detailsHeader;
    public final WrappedLabel hintLabel;

    public WorkbenchMenu(Building workbench) {
        setBackground(StaticSkin.generator.generate(StaticSkin.background));
        defaults().size(300, 700).pad(5);
        add(recipesHeader = new WrappedLabel("Recipes:")).height(HEADER_HEIGHT);
        add(ordersHeader = new WrappedLabel("Orders:")).height(HEADER_HEIGHT);
        add(detailsHeader = new WrappedLabel("Details:")).height(HEADER_HEIGHT).row();
        add(recipeListSection = new RecipeListSection(workbench.get(WorkbenchAspect.class), this));
        add(orderListSection = new OrderListSection(workbench.get(WorkbenchAspect.class), this));
        add(orderDetailsSection = new OrderDetailsSection(workbench.get(WorkbenchAspect.class), this)).row();
        add(hintLabel = new WrappedLabel("")).colspan(3).size(900, 30).height(30).align(Align.left);
    }

    public void initFocus() {
        Actor target = orderListSection;
        if(orderListSection.isEmpty())  target = recipeListSection; // focus recipes, if no orders
        getStage().setKeyboardFocus(target);
    }
}
