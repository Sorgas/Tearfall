package stonering.entity.job.action;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for extraction items from {@link ItemContainerAspect}.
 *
 * @author Alexander on 03.02.2020.
 */
public class GetItemFromContainerAction extends Action {
    private Item item;
    private Entity containerEntity;
    private boolean takeIntoInventory;

    public GetItemFromContainerAction(Item item, Entity container) {
        super(new EntityActionTarget(container, ActionTargetTypeEnum.NEAR));
        startCondition = () -> {
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            if(equipment == null) return FAIL;
            ItemContainerAspect aspect = container.getAspect(ItemContainerAspect.class);
            if(aspect == null) return FAIL;
            if(!aspect.items.contains(item)) return FAIL;
            LocalMap map = GameMvc.model().get(LocalMap.class);
            if(!map.passageMap.inSameArea(containerEntity.position, item.position)) return FAIL;
            equipment.
            // create action to free grab slots
            return OK;
        };
        onFinish = () -> {
            if(takeIntoInventory) {
                container.getAspect(ItemContainerAspect.class).items.remove(item);
                task.performer.getAspect(EquipmentAspect.class).pickupItem(item);
            }
        };
        speedUpdater = () -> {
            // TODO consider performer 'performance', container material and type
            float performanceBonus = task.performer.getAspectOptional(HealthAspect.class).map(aspect -> aspect.properties.get("performance")).orElse(0f);
            return 0.1f;
        };
    }
}
