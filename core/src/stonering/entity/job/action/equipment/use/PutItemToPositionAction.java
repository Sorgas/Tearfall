package stonering.entity.job.action.equipment.use;

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
public class PutItemToPositionAction extends PutItemToDestinationAction {

    public PutItemToPositionAction(Item targetItem, Position targetPosition) {
        super(new PositionActionTarget(targetPosition, ActionTargetTypeEnum.ANY), targetItem);

        onFinish = () -> {
            equipment().hauledItem = null;
            GameMvc.model().get(ItemContainer.class).onMapItemsSystem.addItemToMap(targetItem, targetPosition);
        };
    }
}
