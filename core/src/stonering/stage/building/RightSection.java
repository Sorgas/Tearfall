package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.items.recipe.Ingredient;
import stonering.widget.item.ItemsSelectGrid;

/**
 * Table holds grid of buttons of all items, available for {@link Ingredient}.
 * @author Alexander on 17.02.2020
 */
public class RightSection extends Table {
    ItemsSelectGrid grid;

    public RightSection() {
        grid = new ItemsSelectGrid();
    }
}
