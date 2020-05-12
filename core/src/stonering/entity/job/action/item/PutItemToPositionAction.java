package stonering.entity.job.action.item;

import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;

/**
 * Action for putting item to position on map. Item will be picked up.
 * Action performer should have {@link EquipmentAspect}.
 *
 * @author Alexander on 11.01.2019.
 */
public class PutItemToPositionAction extends PutItemAction {

    public PutItemToPositionAction(Item targetItem, Position targetPosition) {
        super(new PositionActionTarget(targetPosition, ActionTargetTypeEnum.ANY), targetItem);

        onFinish = () -> {
            System.out.println("put item to position finish");
            EquipmentAspect equipmentAspect = task.performer.get(EquipmentAspect.class);
            ItemContainer container = GameMvc.model().get(ItemContainer.class);
            equipmentAspect.hauledItems.remove(targetItem); // remove item from unit
            container.equippedItemsSystem.itemUnequipped(targetItem);
            container.onMapItemsSystem.putItem(targetItem, targetPosition);
        };
    }

    @Override
    public String toString() {
        return super.toString() + "Put " + targetItem.title + " to position";
    }
}
