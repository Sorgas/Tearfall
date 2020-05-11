package stonering.stage.workbench;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.stage.workbench.details.OrderDetailsSection;
import stonering.stage.workbench.orderlist.OrderListSection;
import stonering.stage.workbench.recipelist.RecipeTreeSection;
import stonering.util.global.Initable;
import stonering.widget.util.WrappedLabel;
import stonering.util.global.StaticSkin;

/**
 * Contains three sections: {@link RecipeTreeSection}, {@link OrderListSection}, {@link OrderDetailsSection}.
 * One section can be focused at a time.
 * <p>
 * TODO remove order list label on adding order, update hint label
 *
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu extends Container<Table> implements Initable {
    private final Table table;
    public final RecipeTreeSection recipeTreeSection;
    public final OrderListSection orderListSection;
    public final OrderDetailsSection orderDetailsSection;
    public final List<MenuSection> sections;
    public final Label hintLabel;

    public WorkbenchMenu(Building workbench) {
        WorkbenchAspect workbenchAspect = workbench.get(WorkbenchAspect.class);
        setActor(table = new Table());

        table.setBackground(StaticSkin.generator.generate(StaticSkin.background));

        table.defaults().size(300, 850);
        table.add(recipeTreeSection = new RecipeTreeSection("Recipes", workbenchAspect, this)).padRight(10);
        table.add(orderListSection = new OrderListSection("Orders", workbenchAspect, this)).padRight(10);
        table.add(orderDetailsSection = new OrderDetailsSection("Details", workbenchAspect, this)).row();
        sections = Arrays.asList(recipeTreeSection, orderListSection, orderDetailsSection);

        table.add(hintLabel = new Label("", StaticSkin.getSkin())).colspan(3).height(30).growX().align(Align.center);
        table.setDebug(true, true);
    }

    @Override
    public void init() {
        setFocus(orderListSection.isEmpty() ? recipeTreeSection : orderListSection); // focus recipes, if orders are empty
    }

    public void setFocus(MenuSection section) {
        getStage().setKeyboardFocus(section);
        sections.forEach(menuSection -> menuSection.setBackground(StaticSkin.getColorDrawable(StaticSkin.background))); // reset backgrounds
        section.setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused)); // highlight focused section
    }
}
