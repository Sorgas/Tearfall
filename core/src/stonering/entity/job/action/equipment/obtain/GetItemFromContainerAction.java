package stonering.entity.job.action.equipment.obtain;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.equipment.EquipmentAction;
import stonering.entity.job.action.equipment.use.PutItemToPositionAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for extraction items from {@link ItemContainerAspect}.
 *
 * @author Alexander on 03.02.2020.
 */
public class GetItemFromContainerAction extends EquipmentAction {
    private final Entity containerEntity;
    private final Item item;

    public GetItemFromContainerAction(Item item, Entity containerEntity) {
        super(new EntityActionTarget(containerEntity, ActionTargetTypeEnum.NEAR));
        this.containerEntity = containerEntity;
        this.item = item;

        startCondition = () -> {
            if (equipment().hauledItem != null)
                return addPreAction(new PutItemToPositionAction(equipment().hauledItem, task.performer.position));
            return !validate() ? FAIL : OK;
        };

        onStart = () -> maxProgress = 50;

        onFinish = () -> {
            itemContainer.containedItemsSystem.removeItemFromContainer(item); // remove from container
            equipment().hauledItem = item;
            System.out.println(item + " got from container");
        };
    }

    /**
     * Adds validation of container existence, content and reachability.
     */
    @Override
    protected boolean validate() {
        ItemContainerAspect containerAspect = containerEntity.get(ItemContainerAspect.class);
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return super.validate()
                && containerAspect != null
                && containerAspect.items.contains(item)
                && map.passageMap.inSameArea(this.containerEntity.position, item.position);
    }
}
