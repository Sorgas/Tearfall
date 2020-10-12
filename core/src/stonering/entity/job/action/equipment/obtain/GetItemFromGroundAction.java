package stonering.entity.job.action.equipment.obtain;

import stonering.entity.job.action.equipment.EquipmentAction;
import stonering.entity.job.action.equipment.use.PutItemToPositionAction;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for picking up item. Performer should have {@link EquipmentAspect}.
 * Item is put to special {@link EquipmentAspect#hauledItem} field.
 * Item should be on the ground, (see {@link ObtainItemAction}).
 *
 * @author Alexander on 12.01.2019.
 */
public class GetItemFromGroundAction extends EquipmentAction {
    private Item item;

    public GetItemFromGroundAction(Item item) {
        super(new ItemActionTarget(item));
        this.item = item;

        startCondition = () -> {
            if (equipment().hauledItem != null)
                return addPreAction(new PutItemToPositionAction(equipment().hauledItem, task.performer.position));
            return !validate() ? FAIL : OK;
        };

        onStart = () -> maxProgress = 20;

        onFinish = () -> { // add item to unit
            itemContainer.onMapItemsSystem.removeItemFromMap(item);
            equipment().hauledItem = item;
            System.out.println(item + " got from ground");
        };
    }

    @Override
    protected boolean validate() {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return super.validate()
                && item.position != null
                && itemContainer.itemMap.get(item.position).contains(item)
                && map.passageMap.inSameArea(item.position, task.performer.position);
    }
}
