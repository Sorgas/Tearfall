package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.global.Logger;

import java.util.Objects;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for equipping tool items into appropriate slot.
 * Tool can be equipped, if creature has one grab slot.
 * All other tools in grab slots are unequipped.
 * TODO make two handed tools, probably making main-hand and off-hand, and adding comprehensive requirements to tools.
 */
public class EquipToolItemAction extends Action {
    private Item item;

    public EquipToolItemAction(Item item) {
        super(new SelfActionTarget());
        this.item = item;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
            
        startCondition = () -> { // check that item is on hands for equipping
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            if (!item.isTool()) return Logger.TASKS.logError("Target item is not tool", FAIL);
            if (equipment == null)
                return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", FAIL);
            Item previousToolItem = GetOtherToolEquipped();
            if(previousToolItem != null) {
                task.addFirstPreAction(new UnequipItemAction(previousToolItem));
                return NEW;
            }
            if(equipment.isItemInGrabSlots(item)) return OK; // item is already in some grab slot
            task.addFirstPreAction(new ObtainItemAction(item));
            return NEW;
        };

        onFinish = () -> {
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            ItemContainer container = GameMvc.model().get(ItemContainer.class); 
            equipment.grabSlots.values().stream() // drop all other tools
                    .filter(slot -> slot.grabbedItem != null && slot.grabbedItem.isTool() && slot.grabbedItem != item)
                    .forEach(slot -> {
                        Item wornItem = slot.grabbedItem;
                        slot.grabbedItem = null; 
                        container.equippedItemsSystem.itemUnequipped(wornItem);
                        container.onMapItemsSystem.putItem(wornItem, task.performer.position);
                    });
            //TODO move target item between hands, to maintain main/off hand logic
        };
    }
    
    private Item GetOtherToolEquipped() {
        EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
        return equipment.grabSlots.values().stream()
                .map(slot -> slot.grabbedItem)
                .filter(Objects::nonNull)
                .filter(item -> item != this.item)
                .findFirst().orElse(null);
    }
    
    @Override
    public String toString() {
        return "Equipping action: " + item.title;
    }
}
