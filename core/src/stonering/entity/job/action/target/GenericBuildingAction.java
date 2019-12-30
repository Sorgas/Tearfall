package stonering.entity.job.action.target;

import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.PutItemAction;
import stonering.game.GameMvc;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.List;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

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
    private final BuildingActionTarget target;

    protected GenericBuildingAction(BuildingOrder order) {
        super(new BuildingActionTarget(order));
        target = (BuildingActionTarget) actionTarget;
        this.order = order;
    }

    /**
     * Checks that all ingredients have corresponding item in building position.
     * Creates action for bringing missing item to target position.
     * Creates action for removing blocking items from target position
     */
    @Override
    public ActionConditionStatusEnum check() {
        Logger.TASKS.log("Checking " + this);
        if (target.builderPosition == null && !target.findPositionForBuilder(order, task.performer.position)) return FAIL;
        ItemContainer itemContainer = GameMvc.instance().model().get(ItemContainer.class);
        List<Item> itemsOnSite = itemContainer.getItemsInPosition(target.center);
        if (!itemsOnSite.isEmpty()) return createSiteClearingAction(itemsOnSite.get(0)); // clear site
        List<Item> availableItems = itemContainer.getItemsInPosition(target.builderPosition);
        for (IngredientOrder ingredientOrder : order.parts.values()) {
            if (checkIngredient(ingredientOrder, availableItems)) continue; // ingredient order is fine
            // select new item for ingredient
            if ((ingredientOrder.item == null
                    || !ingredientOrder.itemSelector.checkItem(ingredientOrder.item))
                    && !findItemForIngredient(ingredientOrder)) return FAIL; // item not valid anymore and no new found
            if (!availableItems.contains(ingredientOrder.item)) {
                task.addFirstPreAction(new PutItemAction(ingredientOrder.item, target.builderPosition)); // create action to bring item
                return NEW; // new action is created
            }
            availableItems.remove(ingredientOrder.item);
        }
        return OK; // all ingredients have valid items
        //TODO reset target on fail
    }

    /**
     * Finds item for ingredient's item selector.
     * Updates ingredient order. Returns true, if item for ingredient successfully found.
     */
    private boolean findItemForIngredient(IngredientOrder ingredientOrder) {
        ItemContainer itemContainer = GameMvc.instance().model().get(ItemContainer.class);
        ingredientOrder.item = itemContainer.util.getItemForIngredient(ingredientOrder, task.performer.position);
        if (ingredientOrder.item == null) target.reset();
        return ingredientOrder.item != null;
    }

    /**
     * Creates {@link PutItemAction} for placing blocking item out of target position(to neighbour one).
     */
    private ActionConditionStatusEnum createSiteClearingAction(Item item) {
        Position position = new NeighbourPositionStream(target.center).filterByPassability().stream.findAny().orElse(null);
        if (position == null) {
            target.reset();
            return FAIL;
        }
        task.addFirstPreAction(new PutItemAction(item, position));
        return NEW;
    }

    protected void consumeItems() {
        ItemContainer itemContainer = GameMvc.instance().model().get(ItemContainer.class);
        order.parts.values().stream().map(IngredientOrder::getItem).forEach(item -> {
            itemContainer.onMapItemsSystem.removeItemFromMap(item);
            itemContainer.removeItem(item);
        });
    }

    /**
     * Checks if ingredient item is valid (exists, matches ingredient and lies in target position).
     */
    private boolean checkIngredient(IngredientOrder order, List<Item> availableItems) {
        return order.item != null
                && order.itemSelector.checkItem(order.item) // item matches ingredient
                && availableItems.contains(order.item); // lies in target position
    }
}
