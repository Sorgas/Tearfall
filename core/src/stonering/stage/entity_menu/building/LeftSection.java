package stonering.stage.entity_menu.building;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.geometry.Position;
import stonering.util.lang.StaticSkin;
import stonering.widget.NavigableVerticalGroup;
import stonering.widget.item.SelectedMaterialsWidget;

import java.util.HashMap;
import java.util.Map;

/**
 * Shows what building are designated and how many.
 * Shows material categories required and selected items.
 *
 * @author Alexander on 17.02.2020
 */
public class LeftSection extends Table {
    private BuildingMaterialSelectMenu menu;
    public final Map<String, SelectedMaterialsWidget> widgetMap; // building part name to widget that selects items for this part 
    public final NavigableVerticalGroup<SelectedMaterialsWidget> group;
    private Position position; // position of any building used for items lookup

    public LeftSection(BuildingMaterialSelectMenu menu, Blueprint blueprint, int number, Position position) {
        this.menu = menu;
        this.position = position;
        widgetMap = new HashMap<>();
        defaults().align(Align.topLeft).expandX().fillX();
        add(new Label(createTitle(blueprint, number), StaticSkin.getSkin())).colspan(2).row();
        add(group = createIngredientGroup(blueprint, number)).expandY().colspan(2).row();
        align(Align.topLeft);
    }

    private String createTitle(Blueprint blueprint, int number) {
        return "Building " + (number > 1 ? number + " " + blueprint.title + "s" : blueprint.title);
    }

    private NavigableVerticalGroup<SelectedMaterialsWidget> createIngredientGroup(Blueprint blueprint, int number) {
        NavigableVerticalGroup<SelectedMaterialsWidget> group = new NavigableVerticalGroup<>();
        group.grow();
        blueprint.ingredients.keySet().forEach(key -> {
            System.out.println("creating widget for building part ");
            Ingredient ingredient = blueprint.ingredients.get(key);
            SelectedMaterialsWidget widget = new SelectedMaterialsWidget(ingredient, ingredient.quantity * number, key, menu);
            widgetMap.put(key, widget);
            group.addActor(widget);
        });
        if (blueprint.ingredients.size() == 1)
            widgetMap.values().forEach(widget -> widget.titleLabel.setText("")); // remove title for single part
        return group;
    }

    void setSelected(SelectedMaterialsWidget widget) {
        menu.rightSection.clearGrid();
        menu.rightSection.fillGrid(widget.ingredient, position);
        menu.hintLabel.setText("Selecting items for " + widget.partName);
        group.setSelectedElement(widget);
    }

    /**
     * Selects next unfilled widget for filling, or enables confirmation button.
     */
    public void updateState() {
        SelectedMaterialsWidget nextWidget = widgetMap.values().stream()
                .filter(widget -> widget.targetNumber < widget.number)
                .findFirst().orElse(null);
        if (nextWidget == null) {
            menu.confirmButton.setDisabled(false);
        } else {
            setSelected(nextWidget);
        }
    }
}
