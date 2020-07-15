package stonering.entity.zone;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.Task;
import stonering.entity.job.action.equipment.use.PutItemToPositionAction;
import stonering.enums.ZoneTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.ZoneContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;

import java.util.List;
import java.util.Optional;

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
        super(name, ZoneTypeEnum.STORAGE);
    }

    public void update() {
        //TODO move to system
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
        List<Item> items = GameMvc.model().get(ItemContainer.class).getItemsInPosition(tile);
        return items.stream().noneMatch(item -> selector.checkItem(item));
    }

    /**
     * Creates task for hauling item to the tile.
     */
    private void createHaulingTask(Item item, Position tile) {
        Optional.ofNullable(item)
                .map(item1 -> new PutItemToPositionAction(item, tile))
                .map(Task::new)
                .ifPresent(GameMvc.model().get(TaskContainer.class)::addTask);
    }

    /**
     * Gets items from map, Ensures it is not stored already.
     */
    private Item selectItem(Position position) {
        GameModel model = GameMvc.model();
        List<Item> items = model.get(ItemContainer.class).util.getItemsAvailableBySelector(selector, position);
        if (items.isEmpty()) return null;
        ZoneContainer zoneContainer = model.get(ZoneContainer.class);
        for (Item item : items) {
            Zone zone = zoneContainer.getZone(item.position);
            if (zone == null || zone.type != ZoneTypeEnum.STORAGE || !((StorageZone) zone).selector.checkItem(item)) // item is not stored on appropriate storage.
                return item;
        }
        return null;
    }
}
