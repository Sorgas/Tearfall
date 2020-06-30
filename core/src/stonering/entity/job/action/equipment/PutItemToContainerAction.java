package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;

/**
 * {@link PutItemToDestinationAction} for putting item into {@link ItemContainerAspect}s.
 *
 * @author Alexander on 12.04.2020
 */
public class PutItemToContainerAction extends PutItemToDestinationAction {

    public PutItemToContainerAction(ItemContainerAspect containerAspect, Item targetItem) {
        super(new EntityActionTarget(containerAspect.entity, ActionTargetTypeEnum.NEAR), targetItem);

        onStart = () -> maxProgress = 20;

        onFinish = () -> {
            GameMvc.model().get(UnitContainer.class).equipmentSystem.removeItem(equipment(), targetItem);
            GameMvc.model().get(ItemContainer.class).containedItemsSystem.addItemToContainer(targetItem, containerAspect);
        };
    }
    
    @Override
    public String toString() {
        return super.toString() + " put to container " + targetItem.title;
    }
}
