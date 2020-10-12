package stonering.entity.job.action.equipment.use;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

/**
 * {@link PutItemToDestinationAction} for putting item into {@link ItemContainerAspect}s.
 *
 * @author Alexander on 12.04.2020
 */
public class PutItemToContainerAction extends PutItemToDestinationAction {

    public PutItemToContainerAction(ItemContainerAspect containerAspect, Item targetItem) {
        super(new EntityActionTarget(containerAspect.entity, ActionTargetTypeEnum.NEAR), targetItem);

        onFinish = () -> {
            equipment().hauledItem = null;
            GameMvc.model().get(ItemContainer.class).containedItemsSystem.addItemToContainer(targetItem, containerAspect);
        };
    }
}
