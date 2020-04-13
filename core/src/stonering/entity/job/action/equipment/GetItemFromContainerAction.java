package stonering.entity.job.action.equipment;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for extraction items from {@link ItemContainerAspect}.
 *
 * @author Alexander on 03.02.2020.
 */
public class GetItemFromContainerAction extends Action {
    private Item item;
    private Entity containerEntity;

    public GetItemFromContainerAction(Item item, Entity container) {
        super(new EntityActionTarget(container, ActionTargetTypeEnum.NEAR));
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        LocalMap map = GameMvc.model().get(LocalMap.class);
        ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
        ItemContainerAspect containerAspect = container.get(ItemContainerAspect.class);

        startCondition = () -> {
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            if(equipment == null) return FAIL;
            if(containerAspect == null) return FAIL;
            if(!containerAspect.items.contains(item)) return FAIL;
            if(!map.passageMap.inSameArea(containerEntity.position, item.position)) return FAIL; // container is available
            if (!system.canPickUpItem(equipment, item)) { // if no empty grab slots
                task.addFirstPreAction(new FreeGrabSlotAction()); // free another slot
                return NEW;
            }
            return OK;
        };
        onFinish = () -> {
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            GrabEquipmentSlot slot = system.getSlotForPickingUpItem(equipment, item);
            if (slot != null) {
                itemContainer.containedItemsSystem.removeItemFromContainer(item, containerAspect);
                system.fillGrabSlot(equipment, slot, item);
                return;
            }
            Logger.EQUIPMENT.logError("Slot for picking up item " + item + " not found");
        };

        speedUpdater = () -> {
            // TODO consider performer 'performance', container material and type
            float performanceBonus = task.performer.getOptional(HealthAspect.class).map(aspect -> aspect.properties.get("performance")).orElse(0f);
            return 0.1f;
        };
    }
}
