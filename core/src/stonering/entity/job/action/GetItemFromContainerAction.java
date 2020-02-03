package stonering.entity.job.action;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * @author Alexander on 03.02.2020.
 */
public class GetItemFromContainerAction extends Action {
    private Item item;
    private Entity containerEntity;
    private boolean takeIntoInventory;

    protected GetItemFromContainerAction(Item item, Entity entity) {
        super(new EntityActionTarget(entity, ActionTargetTypeEnum.NEAR));
        startCondition = () -> {
            if(task.performer.hasAspect(EquipmentAspect.class)) return FAIL;
            ItemContainerAspect aspect = entity.getAspect(ItemContainerAspect.class);
            if(aspect == null) return FAIL;
            if(!aspect.items.contains(item)) return FAIL;
            LocalMap map = GameMvc.model().get(LocalMap.class);
            if(map.passageMap.area.get(containerEntity.position) != map.passageMap.area.get(item.position)) return FAIL;
            return OK;
        };
        onFinish = () -> {
            if(takeIntoInventory) {
                entity.getAspect(ItemContainerAspect.class).items.remove(item);
                task.performer.getAspect(EquipmentAspect.class).equipItem(item);
            }
        };
        speedUpdater = () -> {
            // TODO consider performer 'performance', container material and type
            return 0.1f;
        };
    }
}
