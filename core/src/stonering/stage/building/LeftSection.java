package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.util.global.StaticSkin;

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
    }

    private void createIngredientList(Blueprint blueprint, int number) {
        Table table = new Table();
        blueprint.parts.keySet().forEach(part -> {
            table.add(new Label(part, StaticSkin.getSkin()));
            table.add(new Label())
        });
    }
}
