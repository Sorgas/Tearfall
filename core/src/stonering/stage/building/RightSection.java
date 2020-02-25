package stonering.stage.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.geometry.Position;
import stonering.widget.item.ItemsSelectGrid;
import stonering.widget.item.SelectedMaterialsWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Table holds grid of buttons of all items, available for {@link Ingredient}.
 * When button is pressed, Required amount of items is assigned to ingredient. TODO assign single item with Ctrl+(click/E)
 * When no more items left on button, it is disabled.
 *
 * @author Alexander on 17.02.2020
 */
public class RightSection extends Table {
    BuildingMaterialSelectMenu menu;
    ItemsSelectGrid grid;

    public RightSection(BuildingMaterialSelectMenu menu) {
        this.menu = menu;
        add(grid = new ItemsSelectGrid(8, 8)).fill().expand();
    }

    public void fill(Ingredient ingredient, Position position) {
        grid.fillForIngredient(ingredient, position);
        // moves some items from button to widget
        // 
        grid.commonHandler = button -> {
            SelectedMaterialsWidget widget = menu.leftSection.selectedWidget;
            List<Item> items = button.items;
            if(items.stream().allMatch(item -> item.tags.contains(ingredient.tag) && ingredient.itemTypes.contains(item.type.name)));
            List<Item> itemsToMove = new ArrayList<>();
            if(Gdx.input.isButtonPressed(Input.Keys.CONTROL_LEFT)) {
                itemsToMove.add(items.get(0)); // TODO get nearest
            } else {
                itemsToMove.addAll(items.subList(0, Math.min(widget.targetNumber - widget.number, items.size())));
            }
            for (Item item : itemsToMove) {
                widget.addItem(item);
            }
        };
    }
}
