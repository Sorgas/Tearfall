package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.StaticSkin;

/**
 * Table widget, that shows some title,
 * number and category of required items, and number and types of selected items.
 *
 * @author Alexander on 17.02.2020
 */
public class SelectedMaterialsWidget extends Table {
    HorizontalGroup group;

    public SelectedMaterialsWidget(Ingredient ingredient, int number, String title) {
        add(new Label(title, StaticSkin.getSkin())).left().row();
        add(new Label(ingredient.text, StaticSkin.getSkin())).left().row();
        add(group = new HorizontalGroup().left()).fillX();
    }

    public void addItem(Item item) {

    }

}
