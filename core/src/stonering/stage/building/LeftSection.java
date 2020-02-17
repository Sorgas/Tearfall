package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.StaticSkin;
import stonering.widget.item.SelectedMaterialsWidget;

/**
 * Shows what building are designated and how many.
 * Shows material categories required and selected items.
 *
 * @author Alexander on 17.02.2020
 */
public class LeftSection extends Table {

    public LeftSection(Blueprint blueprint, int number) {
        createTitle(blueprint, number);
    }

    private void createTitle(Blueprint blueprint, int number) {
        String text = "Building " + (number > 1 ?  + number + blueprint.title + "s" : blueprint.title);
        add(new Label(text, StaticSkin.getSkin()));
        createIngredientList(blueprint, number);
    }

    /**
     * Creates widgets for each part of a building.
     */
    private void createIngredientList(Blueprint blueprint, int number) {
        Table table = new Table();
        blueprint.parts.keySet().forEach(part -> {
            Ingredient ingredient = blueprint.parts.get(part);
            table.add(new SelectedMaterialsWidget(ingredient, ingredient.quantity * number, part));
        });
    }
}
