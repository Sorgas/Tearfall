package stonering.entity.job.action.equipment;

import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.geometry.Position;

/**
 * Action for putting item to position on map. Item will be picked up.
 * Action performer should have {@link EquipmentAspect}.
 *
 * @author Alexander on 11.01.2019.
 */
public class PutItemToPositionAction extends PutItemToDestinationAction {

    public PutItemToPositionAction(Item targetItem, Position targetPosition) {
        super(new PositionActionTarget(targetPosition, ActionTargetTypeEnum.ANY), targetItem);

        onStart = () -> maxProgress = 20;

        onFinish = () -> {
            GameMvc.model().get(UnitContainer.class).equipmentSystem.removeItemFromBuffer(equipment());
            GameMvc.model().get(ItemContainer.class).onMapItemsSystem.addItemToMap(targetItem, targetPosition);
        };
    }

    @Override
    public String toString() {
        return super.toString() + "Put " + targetItem.title + " to position";
    }
}
