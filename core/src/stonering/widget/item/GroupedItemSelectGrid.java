package stonering.widget.item;

import stonering.entity.item.ItemGroupingKey;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.OnMapItemsStream;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * {@link ItemButtonWidget} that shows buttons of items organized in grid.
 * When all items from some button are removed, this button is disabled.
 * Groups items by type and material.
 * TODO add item stats tooltip
 *
 * @author Alexander on 17.02.2020
 */
public class GroupedItemSelectGrid extends ActorGrid<StackedItemSquareButton> implements ItemButtonWidget {
    private Map<ItemGroupingKey, StackedItemSquareButton> buttonMap;
    public Consumer<StackedItemSquareButton> commonHandler; // handles all buttons

    public GroupedItemSelectGrid(int cellWidth, int cellHeight) {
        super(cellWidth);
        buttonMap = new HashMap<>();
        commonHandler = button -> {};
        defaults().pad(5).size(StackedItemSquareButton.SIZE, StackedItemSquareButton.SIZE);
        top().left();
        //TODO add table background
    }

    public void fillForIngredient(Ingredient ingredient, Position position) {
        refillItems(new OnMapItemsStream()
                .filterByReachability(position)
                .stream.filter(ingredient::checkItem).collect(Collectors.toList()));
    }

    public void setAllButtonsDisabled(boolean disabled) {
//        for (Cell<StackedItemSquareButton>[] row : gridCells) {
//            for (Cell<StackedItemSquareButton> cell : row) {
//                if (cell != null && cell.getActor() != null) cell.getActor().setDisabled(disabled);
//            }
//        }
    }

    @Override
    public void clearGrid() {
        super.clearGrid();
        buttonMap.clear();
    }

    @Override
    public void buttonEmpty(StackedItemSquareButton button) {
        button.setDisabled(true);
    }

    @Override
    public void buttonAdded(StackedItemSquareButton button) {
        addActorToGrid(button);
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
