package stonering.entity.job.action;

import stonering.entity.Entity;
import stonering.entity.building.aspects.FuelConsumerAspect;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.FuelAspect;
import stonering.entity.item.selectors.FuelItemSelector;
import stonering.entity.job.action.equipment.ItemPickupAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for putting fuel items to entities with {@link FuelConsumerAspect}.
 *
 * @author Alexander on 18.09.2019.
 */
public class FuelingAciton extends Action {
    public Item targetItem;

    public FuelingAciton(Entity target) {
        super(new EntityActionTarget(target, ActionTargetTypeEnum.NEAR));
        startCondition = () -> {
            if (!((EntityActionTarget) this.target).entity.has(FuelConsumerAspect.class))
                return FAIL; // invalid entity
            if (targetItem == null && (targetItem = lookupFuelItem()) == null) return FAIL; // no fuel item available
            if (!task.performer.get(EquipmentAspect.class).items.contains(targetItem)) {
                task.addFirstPreAction(new ItemPickupAction(targetItem));
                return NEW;
            }
            return OK; // performer has item in inventory
        };

        onFinish = () -> {
            task.performer.get(EquipmentAspect.class).dropItem(targetItem);
            ((EntityActionTarget) this.target).entity.get(FuelConsumerAspect.class).acceptFuel(targetItem);
        };
    }

    private Item lookupFuelItem() {
        Item foundItem = task.performer.get(EquipmentAspect.class).items.stream().filter(item -> item.has(FuelAspect.class)
                && item.get(FuelAspect.class).isEnabled()).findFirst().orElse(null); // item from inventory
        if (foundItem != null) return foundItem;
        return GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(new FuelItemSelector(), task.performer.position);
    }
}
