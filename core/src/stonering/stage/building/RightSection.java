package stonering.stage.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.geometry.Position;
import stonering.widget.item.ItemsSelectGrid;
import stonering.widget.item.SelectedMaterialsWidget;

import java.util.List;

/**
 * Table holds grid of buttons of all items, available for {@link Ingredient}.
 * When button is pressed, Required amount of items is assigned to ingredient. Pressing button with Ctrl will add one item.
 * When no more items left on button, it is disabled. All buttons in grid are disabled, when ingredient for building is full
 *
 * @author Alexander on 17.02.2020
 */
public class RightSection extends Table {
    BuildingMaterialSelectMenu menu;
    ItemsSelectGrid grid;

    public RightSection(BuildingMaterialSelectMenu menu) {
        this.menu = menu;
        add(grid = new ItemsSelectGrid(8, 8)).fill().expand().top().left();
    }

    public void fill(Ingredient ingredient, Position position) {
        grid.fillForIngredient(ingredient, position);
        grid.commonHandler = button -> {
            SelectedMaterialsWidget widget = menu.leftSection.selectedWidget;
            int number = Gdx.input.isButtonPressed(Input.Keys.CONTROL_LEFT)
                    ? 1 // add by one
                    : Math.min(widget.targetNumber - widget.number, button.items.size()); // add max possible
            List<Item> itemsToMove = button.items.subList(0, number);
            button.items.removeAll(itemsToMove); // remove from button
            button.updateLabel();
            widget.addItems(itemsToMove); // add to widget
            if(widget.targetNumber == widget.number) grid.setAllButtonsDisabled(true); // ingredient is full
        };
    }
}
