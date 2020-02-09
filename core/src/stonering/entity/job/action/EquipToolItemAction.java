package stonering.entity.job.action;

import stonering.entity.item.Item;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for equipping tool items into appropriate slot.
 * Tool can be equipped, if creature has one grab slot.
 * TODO make two handed tools, probably making main-hand and off-hand, and adding comprehensive requirements to tools.
 */
public class EquipToolItemAction extends Action {
    private Item item;

    public EquipToolItemAction(Item item) {
        super(new SelfActionTarget());
        this.item = item;

        startCondition = () -> { // check that item is on hands for equipping
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            if (!item.isTool()) return Logger.TASKS.logError("Target item is not tool", FAIL);
            if (equipment == null)
                return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", FAIL);
            if(equipment.isItemInGrabSlots(item)) return OK; // item is already in some grab slot
            task.addFirstPreAction(new ObtainItemAction(item));
            return NEW;
        };

        //TODO onFinish: move target item between hands, to maintain main/off hand logic
    }

    @Override
    public String toString() {
        return "Equipping action: " + item.title;
    }
}
