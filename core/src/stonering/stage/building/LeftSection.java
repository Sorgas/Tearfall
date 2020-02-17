package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.StaticSkin;
import stonering.widget.item.SelectedMaterialsWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows what building are designated and how many.
 * Shows material categories required and selected items.
 *
 * @author Alexander on 17.02.2020
 */
public class LeftSection extends Table {
    List<SelectedMaterialsWidget> list;

    public LeftSection(Blueprint blueprint, int number) {
        list = new ArrayList<>();
        createTitle(blueprint, number);
    }

    private void createTitle(Blueprint blueprint, int number) {
        defaults().left();
        String text = "Building " + (number > 1 ? number + " " + blueprint.title + "s" : blueprint.title);
        add(new Label(text, StaticSkin.getSkin())).row();
        add(createIngredientList(blueprint, number));
    }

    /**
     * Creates widgets for each part of a building.
     */
    private Table createIngredientList(Blueprint blueprint, int number) {
        Table table = new Table();
        blueprint.parts.keySet().forEach(part -> {
            Ingredient ingredient = blueprint.parts.get(part);
            SelectedMaterialsWidget selectedMaterialsWidget = new SelectedMaterialsWidget(ingredient, ingredient.quantity * number, part);
            list.add(selectedMaterialsWidget);
            table.add(selectedMaterialsWidget);
        });
        return table;
    }

    void setSelected(String string) {

    }
}
