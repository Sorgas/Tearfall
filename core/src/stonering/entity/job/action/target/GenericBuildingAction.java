package stonering.entity.job.action.target;

import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.PutItemAction;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.global.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.PASSABLE;

/**
 * Action for creating constructions and buildings on map.
 * Target position should be clear from items.
 * Building can be created in the same z-level cell next to a builder,
 * or if builder can step into cell with construction after completing it (for constructions).
 * <p>
 * Materials for construction should be brought to the cell where builder can stand.
 *
 * @author Alexander on 02.10.2019.
 */
public abstract class GenericBuildingAction extends Action {
    protected final BuildingOrder order;

    protected GenericBuildingAction(BuildingOrder order) {
        super(new BuildingActionTarget(order));
        this.order = order;
    }

    /**
     * Checks that all ingredients have corresponding item in building position.
     * Creates action for bringing missing item to target position.
     * Creates action for removing blocking items from target position
     */
    @Override
    public int check() {
        Logger.TASKS.log("Checking " + this);
        BuildingActionTarget target = (BuildingActionTarget) actionTarget;
        if (target.builderPosition == null && !target.findPositionForBuilder(order, task.performer.position))
            return FAIL; // cannot find position for builder to stand
        ItemContainer itemContainer = GameMvc.instance().model().get(ItemContainer.class);
        List<Item> itemsOnSite = itemContainer.getItemsInPosition(target.center);
        if (!itemsOnSite.isEmpty()) return createSiteClearingAction(itemsOnSite.get(0)); // clear site
        List<Item> availableItems = itemContainer.getItemsInPosition(target.builderPosition);
        for (IngredientOrder order : order.parts.values()) {
            if(order.item != null) availableItems.remove(order.item);
            if (checkIngredient(order, availableItems)) continue; // ingredient order is fine
            if (order.item != null)
                System.out.println("'spoiled' item in ingredient order"); // free item TODO locking items in container
            order.item = itemContainer.util.getItemForIngredient(order, task.performer.position);
            if (order.item == null) {
                target.reset();
                return FAIL; // no valid item found
            }
            if (availableItems.contains(order.item)) continue; // item in WB, no actions required
            task.addFirstPreAction(new PutItemAction(order.item, target.builderPosition)); // create action to bring item
            return NEW; // new action is created
        }
        return OK; // all ingredients have valid items
        //TODO reset target on fail
    }

    /**
     * Creates {@link PutItemAction} for placing blocking item out of target position(to neighbour one).
     */
    private int createSiteClearingAction(Item item) {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        PutItemAction putItemAction = new PutItemAction(item, localMap.getAnyNeighbourPosition(actionTarget.getPosition(), PASSABLE));
        task.addFirstPreAction(putItemAction);
        return NEW;
    }

    protected void consumeItems() {
        ItemContainer itemContainer = GameMvc.instance().model().get(ItemContainer.class);
        order.parts.values().stream().map(IngredientOrder::getItem).forEach(item ->
                {
                    itemContainer.onMapItemsSystem.removeItemFromMap(item);
                    itemContainer.removeItem(item);
                });
    }

    /**
     * Checks if ingredient item is valid (matches ingredient and lies in target position).
     */
    private boolean checkIngredient(IngredientOrder order, List<Item> availableItems) {
        return order.item != null
                && order.itemSelector.checkItem(order.item)
                && availableItems.contains(order.item);
    }
}
