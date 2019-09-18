package stonering.entity.zone;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.PutItemToContainerAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.ItemContainer;
import stonering.game.model.system.ZonesContainer;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.util.geometry.Position;

import java.util.List;
import java.util.Set;

/**
 * Zone that generates tasks for hauling items and dropping them into it.
 * Can be configured for storing specific types of items.
 * <p>
 * Tends to have one item in each cell, despite that unlimited amount of items can exist in one cell.
 * Does not try to split larger number of desired items from one cell, just fill empty ones.
 *
 * @author Alexander_Kuzyakov on 04.07.2019.
 */
public class StorageZone extends Zone {
    private ItemSelector selector;

    public StorageZone(String name) {
        super(name);
    }

    public StorageZone(String name, Set<Position> tiles) {
        super(name, tiles);
    }

    @Override
    public void turn() {
        for (Position tile : tiles) {
            if (tileIsEmpty(tile)) {
                createHaulingTask(selectItem(tile), tile);
            }
        }
    }

    /**
     * Checks tile for having desired item in it.
     */
    private boolean tileIsEmpty(Position tile) {
        List<Item> items = GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(tile);
        return items.stream().noneMatch(item -> selector.checkItem(item));
    }

    /**
     * Creates task for hauling item to the tile.
     */
    private void createHaulingTask(Item item, Position tile) {
        if (item == null) return;
        Action action = new PutItemToContainerAction(item, tile);
        Task task = new Task("Store " + item.getTitle(), TaskTypesEnum.OTHER, action, 1);
        GameMvc.instance().getModel().get(TaskContainer.class).addTask(task);
    }

    /**
     * Gets items from map, Ensures it is not stored already.
     */
    private Item selectItem(Position position) {
        GameModel model = GameMvc.instance().getModel();
        List<Item> items = model.get(ItemContainer.class).getItemsAvailableBySelector(selector, position);
        if (items.isEmpty()) return null;
        ZonesContainer zonesContainer = model.get(ZonesContainer.class);
        for (Item item : items) {
            Zone zone = zonesContainer.getZone(item.position);
            if (zone == null || zone.type != ZoneTypesEnum.STORAGE || !((StorageZone) zone).selector.checkItem(item)) // item is not stored on appropriate storage.
                return item;
        }
        return null;
    }
}
