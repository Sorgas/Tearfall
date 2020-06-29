package stonering.entity.job.action.equipment;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for extraction items from {@link ItemContainerAspect}.
 *
 * @author Alexander on 03.02.2020.
 */
public class GetItemFromContainerAction extends EquipmentAction {
    private Entity containerEntity;

    public GetItemFromContainerAction(Item item, Entity containerEntity) {
        super(new EntityActionTarget(containerEntity, ActionTargetTypeEnum.NEAR));
        this.containerEntity = containerEntity;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        LocalMap map = GameMvc.model().get(LocalMap.class);
        ItemContainerAspect containerAspect = containerEntity.get(ItemContainerAspect.class);

        startCondition = () -> {
            if(!validate()) return FAIL;
            if(containerAspect == null) return FAIL;
            if(!containerAspect.items.contains(item)) return FAIL;
            if(!map.passageMap.inSameArea(this.containerEntity.position, item.position)) return FAIL; // container is available
            if (equipment().grabSlotStream().noneMatch(slot -> slot.grabbedItem == null)) // if no empty grab slots
                return addPreAction(new FreeGrabSlotAction()); // free another slot
            return OK;
        };

        onStart = () -> maxProgress = 50;
        
        onFinish = () -> {
            equipment().grabSlotStream()
                    .filter(slot -> slot.grabbedItem == null)
                    .findFirst()
                    .ifPresent(slot -> {
                        itemContainer.containedItemsSystem.removeItemFromContainer(item); // remove from container
                        system.fillGrabSlot(equipment(), slot, item); // add to equipment
                    });
            Logger.EQUIPMENT.logError("Slot for picking up item " + item + " not found");
        };
    }
}
