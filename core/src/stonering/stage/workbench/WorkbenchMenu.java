package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
public class WorkbenchMenu extends Container<Table> {
    private final Table table;
    public final RecipeListSection recipeListSection;
    public final OrderListSection orderListSection;
    public final OrderDetailsSection orderDetailsSection;
    public final WrappedLabel ordersHeader;
    public final WrappedLabel detailsHeader;
    public final WrappedLabel hintLabel;

    public WorkbenchMenu(Building workbench) {
        WorkbenchAspect workbenchAspect = workbench.get(WorkbenchAspect.class);
        setActor(table = new Table());

        table.setBackground(StaticSkin.generator.generate(StaticSkin.background));
//        table.pad(10);

        table.defaults().size(300, 850).pad(10);
        table.add(new Label("qwer", StaticSkin.getSkin()));
        table.add(ordersHeader = new WrappedLabel("Orders:")).height(100);
        table.add(detailsHeader = new WrappedLabel("Details:")).height(100).row();

        table.add(recipeListSection = new RecipeListSection("Recipes:", workbenchAspect, this));
        table.add(orderListSection = new OrderListSection(workbenchAspect, this));
        table.add(orderDetailsSection = new OrderDetailsSection(workbenchAspect, this)).row();

        table.add(hintLabel = new WrappedLabel("")).colspan(3).size(900, 30).height(30).align(Align.left);
        table.setDebug(true, true);
    }

    public void initFocus() {
        Actor target = orderListSection;
        if(orderListSection.isEmpty())  target = recipeListSection; // focus recipes, if no orders
        getStage().setKeyboardFocus(target);
    }
}
