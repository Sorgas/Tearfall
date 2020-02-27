package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.entity.item.ItemGroupingKey;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * {@link ItemButtonWidget} that shows buttons of items organized in grid.
 * Is a {@link VerticalGroup} of {@link HorizontalGroup}s.
 * Items can only be added to this widget.
 * TODO add item stats tooltip
 *
 * @author Alexander on 17.02.2020
 */
public class ItemsSelectGrid extends ActorGrid<StackedItemSquareButton> implements ItemButtonWidget {
    private Map<ItemGroupingKey, StackedItemSquareButton> buttonMap;
    public Consumer<StackedItemSquareButton> commonHandler; // handles all buttons
    
    public ItemsSelectGrid(int cellWidth, int cellHeight) {
        super(cellWidth, cellHeight);
        buttonMap = new HashMap<>();
        commonHandler = button -> {};
        defaults().pad(5).size(StackedItemSquareButton.SIZE, StackedItemSquareButton.SIZE);
        top().left();
        super.init();
        // set table background
    }

    public void fillForIngredient(Ingredient ingredient, Position position) {
        refillItems(new ItemsStream()
                .filterByReachability(position)
                .stream.filter(ingredient::checkItem).collect(Collectors.toList()));
    }
    
    public void setAllButtonsDisabled(boolean disabled) {
        for (Cell<StackedItemSquareButton>[] row : gridCells) {
            for (Cell<StackedItemSquareButton> cell : row) {
                if(cell != null) cell.getActor().setDisabled(disabled);
            }
        }
    }

    @Override
    public void addButton(StackedItemSquareButton button) {
        addActorToGrid(button);
    }

    @Override
    public void buttonEmpty(StackedItemSquareButton button) {
        // remove item or disable it?
    }

    @Override
    public void processButtonPress(StackedItemSquareButton button) {
        commonHandler.accept(button);
    }

    @Override
    public Map<ItemGroupingKey, StackedItemSquareButton> getButtonMap() {
        return buttonMap;
    }
}
