package stonering.entity.job.action;

import stonering.entity.item.Item;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.items.ItemTagEnum;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action from drinking items that are drinkable ({@link ItemTagEnum}).
 *
 * @author Alexander on 09.10.2019.
 */
public class DrinkAction extends Action{
    private Item item;

    public DrinkAction(Item item) {
        super(new ItemActionTarget(item));
        this.item = item;
        startCondition = () -> {
            if(!item.tags.contains(ItemTagEnum.DRINKABLE)) return FAIL; // item is not edible
            if(checkBetterDrink()) return FAIL; // better food is available, recreate.
            //TODO if tables available, use
            //TODO if dishes available, use
            Unit performer = task.performer;
            if(performer.has(EquipmentAspect.class)) {
                if(performer.get(EquipmentAspect.class).items.contains(item)) return OK;
            } else {

            }
            return OK;
        };
    }

    private boolean checkBetterDrink() {
        //TODO if target is spoiled and non-spoiled is available
        //TODO if target is spoiled and water source is available
        return false;
    }
}
