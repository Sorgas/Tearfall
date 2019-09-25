package stonering.widget.lists;

import stonering.entity.crafting.BuildingComponent;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.game.model.system.ItemContainer;
import stonering.widget.Hideable;
import stonering.entity.item.Item;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * List that shows item, appropriate for building.
 * If no item found for building, shows shows indication line which cannot be selected.
 * Takes information from {@link DesignationsController}
 *
 * @author Alexander on 03.07.2018.
 */
public class MaterialSelectList extends ItemsCountList implements Hideable {
    public boolean active = true; // prevents clicking when no items found

    /**
     * Fills this list for given crafting or building step.
     */
    public void fillForCraftingStep(BuildingComponent step, Position position) {
        clearItems();
        List<Item> items = GameMvc.instance().getModel().get(ItemContainer.class).getAvailableMaterialsForBuildingStep(step, position);
        if (!items.isEmpty()) {
            addItems(items);
            setSelectedIndex(-1); //change event is not fired without this.
            setSelectedIndex(0);
        } else {
            setItems(new ListItem("No item available.", null));
            active = false;
        }
    }

    @Override
    public void show() {
        GameMvc.instance().getView().getUiDrawer().getToolbar().addMenu(this);
    }

    @Override
    public void hide() {
        GameMvc.instance().getView().getUiDrawer().getToolbar().hideMenu(this);
    }
}
