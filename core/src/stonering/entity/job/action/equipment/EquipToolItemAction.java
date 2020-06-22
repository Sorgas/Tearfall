package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.job.action.ItemAction;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for equipping tool items into appropriate slot.
 * Tool can be equipped, if creature has one grab slot.
 * All other tools in grab slots are unequipped.
 * TODO make two handed tools, probably making main-hand and off-hand, and adding comprehensive requirements to tools.
 */
public class EquipToolItemAction extends ItemAction {
    private Item item;

    public EquipToolItemAction(Item item) {
        super(new SelfActionTarget());
        this.item = item;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        
        startCondition = () -> { // check that item is on hands for equipping
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            
            if (equipment == null) 
                return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", FAIL);
            if (item.type.tool == null) 
                return Logger.TASKS.logError("Target item is not tool", FAIL);
            
            if(equipment.isItemInGrabSlots(item)) return OK; // item is already in some grab slot
            
            return addPreAction(new ObtainItemAction(item)); // create action for getting target item 
        };

        onFinish = () -> {
            task.performer.getOptional(EquipmentAspect.class).stream()
                    .flatMap(equipment -> equipment.grabSlots.values().stream())
                    .filter(slot -> slot.grabbedItem != null && slot.grabbedItem.type.tool != null && slot.grabbedItem != item)
                    .forEach(slot -> {
                        Item wornItem = system.freeGrabSlot(slot); // remove from hands
                        container.onMapItemsSystem.putItem(wornItem, task.performer.position); // put to map
                    });
            System.out.println("tool equipped");
            //TODO move target item between hands, to maintain main/off hand logic
        };
    }
    
    @Override
    public String toString() {
        return "Tool equipping action: " + item.title;
    }
}
