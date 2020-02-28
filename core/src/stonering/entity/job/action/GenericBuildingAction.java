package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.Item;
import stonering.entity.job.action.equipment.PutItemAction;
import stonering.entity.job.action.target.BuildingActionTarget;
import stonering.game.GameMvc;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.Collection;
import java.util.List;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for creating constructions and buildings on map.
 * Building is designated with {@link stonering.entity.job.designation.BuildingDesignation} with {@link BuildingOrder}.
 * Target position should be clear from items.
 * Building can be created in the same z-level cell next to a builder,
 * or if builder can step into cell with construction after completing it (for constructing stairs and floors on SPACE cells).
 * <p>
 * Position for builder to stand during building is selected in the beginning of an action. This selection can fail action.
 * Materials for construction should be brought to selected builder position.
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

        startCondition = () -> {
            Logger.TASKS.log("Checking " + this);
            if (target.builderPosition == null && !target.findPositionForBuilder(order, task.performer.position)) return FAIL; // cannot find position for builder
            ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
            
            // check site is clear
            List<Item> itemsOnSite = itemContainer.getItemsInPosition(target.center);
            if (!itemsOnSite.isEmpty()) return createSiteClearingAction(itemsOnSite.get(0));
            
            // check material items
            List<Item> availableItems = itemContainer.getItemsInPosition(target.builderPosition);
            for (IngredientOrder ingredientOrder : order.parts.values()) {
                for (int i = 0; i < ingredientOrder.items.size(); i++) {
                    Item item = ingredientOrder.items.get(i);
                    // check item is fine
                    if (!ingredientOrder.itemSelector.checkItem(item)) {
                        item = findItemForIngredient(ingredientOrder);
                        if (item == null) return FAIL;
                        ingredientOrder.items.set(i, item);
                    }
                    // check item is brought
                    if (!availableItems.contains(item)) {
                        task.addFirstPreAction(new PutItemAction(item, target.builderPosition)); // create action to bring item
                        return NEW; // new action is created
                    }
                    availableItems.remove(item);
                }
            }
            return OK; // all ingredients have valid items
            //TODO reset target on fail
        };
    }
    
    /**
     * Finds item for ingredient's item selector.
     * Updates ingredient order. Returns true, if item for ingredient successfully found.
     */
    private Item findItemForIngredient(IngredientOrder ingredientOrder) {
        return GameMvc.model().get(ItemContainer.class).util.getItemForIngredient(ingredientOrder, task.performer.position);
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
        ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
        order.parts.values().stream().map(order -> order.items).flatMap(Collection::stream).forEach(item -> {
            itemContainer.onMapItemsSystem.removeItemFromMap(item);
            itemContainer.removeItem(item);
        });
    }
}
