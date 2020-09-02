package stonering.stage.entity_menu.building.workbench;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.stage.entity_menu.building.MenuSection;
import stonering.stage.entity_menu.building.workbench.details.OrderDetailsSection;
import stonering.stage.entity_menu.building.workbench.orderlist.OrderListSection;
import stonering.stage.entity_menu.building.workbench.recipelist.RecipeTreeSection;
import stonering.util.lang.Initable;
import stonering.widget.Restoreable;
import stonering.util.lang.StaticSkin;
import stonering.widget.util.KeyNotifierListener;

/**
 * Contains three sections: {@link RecipeTreeSection}, {@link OrderListSection}, {@link OrderDetailsSection}.
 * One section can be focused at a time.
 * 
 * TODO remove order list label on adding order, update hint label
 *
 * @author Alexander on 12.08.2019.
 */
public class WorkbenchMenu extends Container<Table> implements Initable, Restoreable {
    private final Table table;
    public final RecipeTreeSection recipeTreeSection;
    public final OrderListSection orderListSection;
    public final OrderDetailsSection orderDetailsSection;

    public final List<MenuSection> sections;
    public final Label hintLabel;
    private MenuSection focused;

    public WorkbenchMenu(Building workbench) {
        WorkbenchAspect workbenchAspect = workbench.get(WorkbenchAspect.class);
        setActor(table = new Table());

        table.setBackground(StaticSkin.generator.generate(StaticSkin.background));
        table.defaults().size(300, 870);
        table.add(recipeTreeSection = new RecipeTreeSection("Recipes", workbenchAspect, this));
        table.add(orderListSection = new OrderListSection("Orders", workbenchAspect, this));
        table.add(orderDetailsSection = new OrderDetailsSection("Details", workbenchAspect, this)).row();
        sections = Arrays.asList(recipeTreeSection, orderListSection, orderDetailsSection);

        table.add(hintLabel = new Label("", StaticSkin.getSkin())).colspan(3).height(30).growX().align(Align.left);
        table.setDebug(true, true);
        addListener(new KeyNotifierListener(() -> this.focused));
    }

    @Override
    public void init() {
        setFocus(orderListSection.isEmpty() ? recipeTreeSection : orderListSection); // focus recipes, if orders are empty
    }

    public void setFocus(MenuSection section) {
        focused = section;
        sections.forEach(menuSection -> menuSection.setBackground(StaticSkin.getColorDrawable(StaticSkin.background))); // reset backgrounds
        section.setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused)); // highlight focused section
        hintLabel.setText(section.getHint());
    }

    @Override
    public void saveState() {}

    @Override
    public void restoreState() {
        if(focused != null) setFocus(focused);
    }
}
