package stonering.entity.job.action;

import stonering.entity.Entity;
import stonering.entity.building.aspects.FuelConsumerAspect;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.FuelAspect;
import stonering.entity.item.selectors.FuelItemSelector;
import stonering.entity.job.action.equipment.EquipmentAction;
import stonering.entity.job.action.equipment.obtain.ObtainItemAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for putting fuel items to entities with {@link FuelConsumerAspect}.
 *
 * @author Alexander on 18.09.2019.
 */
public class FuelingAciton extends EquipmentAction {
    public Item targetItem;

    public FuelingAciton(Entity target) {
        super(new EntityActionTarget(target, ActionTargetTypeEnum.NEAR));
        startCondition = () -> {
            if (!((EntityActionTarget) this.target).entity.has(FuelConsumerAspect.class))
                return FAIL; // invalid entity
            if (targetItem == null && (targetItem = lookupFuelItem()) == null) return FAIL; // no fuel item available
            if (!equipment().items.contains(targetItem)) 
                return addPreAction(new ObtainItemAction(targetItem));
            return OK; // performer has item in inventory
        };

        onFinish = () -> {
            GameMvc.model().get(UnitContainer.class).equipmentSystem.removeItem(equipment(), targetItem); // remove from unit
            ((EntityActionTarget) this.target).entity.get(FuelConsumerAspect.class).acceptFuel(targetItem); // fuel consumer
        };
    }

    private Item lookupFuelItem() {
        Item foundItem = equipment().items.stream().filter(item -> item.has(FuelAspect.class)
                && item.get(FuelAspect.class).isEnabled()).findFirst().orElse(null); // item from inventory
        if (foundItem != null) return foundItem;
        return GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(new FuelItemSelector(), task.performer.position);
    }
}
