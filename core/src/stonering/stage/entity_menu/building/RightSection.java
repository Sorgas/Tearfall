package stonering.stage.entity_menu.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.geometry.Position;
import stonering.widget.item.GroupedItemSelectGrid;
import stonering.widget.item.SelectedMaterialsWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Table holds grid of buttons of all items, available for {@link Ingredient}.
 * When button is pressed, Required amount of items is assigned to ingredient. Pressing button with Ctrl will add one item.
 * When no more items left on button, it is disabled. All buttons in grid are disabled, when ingredient for building is full
 *
 * @author Alexander on 17.02.2020
 */
public class RightSection extends Table {
    public final BuildingMaterialSelectMenu menu;
    public final GroupedItemSelectGrid grid;

    public RightSection(BuildingMaterialSelectMenu menu) {
        this.menu = menu;
        add(grid = new GroupedItemSelectGrid(8, 8)).fill().expand().top().left();
    }

    public void fillGrid(Ingredient ingredient, Position position) {
        grid.fillForIngredient(ingredient, position);
        grid.commonHandler = button -> { // handler for item buttons
            SelectedMaterialsWidget widget = menu.leftSection.group.getSelectedElement();
            int number = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
                    ? 1 // add by one
                    : Math.min(widget.targetNumber - widget.number, button.items.size()); // add max possible
            System.out.println("number to select: " + number);
            List<Item> itemsToMove = new ArrayList<>(button.items.subList(0, number));
            button.items.removeAll(itemsToMove); // remove from button
            button.updateLabel();

            for (int i = 0; i < itemsToMove.size(); i++) {
                Item item = itemsToMove.get(i);
                item.locked = true;
                widget.addItem(item);
            }

            if(widget.targetNumber == widget.number) {
                System.out.println("disabling all buttons");
                grid.setAllButtonsDisabled(true); // ingredient is full
                menu.leftSection.updateState();
            }
        };
    }

    public void clearGrid() {
        grid.clearGrid();
    }
}
