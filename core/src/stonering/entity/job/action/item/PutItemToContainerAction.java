package stonering.entity.job.action.item;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

/**
 * {@link PutItemAction} for putting item into {@link ItemContainerAspect}s.
 *
 * @author Alexander on 12.04.2020
 */
public class PutItemToContainerAction extends PutItemAction {

    public PutItemToContainerAction(ItemContainerAspect containerAspect, Item targetItem) {
        super(new EntityActionTarget(containerAspect.entity, ActionTargetTypeEnum.NEAR), targetItem);

        onFinish = () -> {
            EquipmentAspect equipmentAspect = task.performer.get(EquipmentAspect.class);
            ItemContainer container = GameMvc.model().get(ItemContainer.class);
            equipmentAspect.items.remove(targetItem); // remove item from unit
            container.equippedItemsSystem.itemUnequipped(targetItem);

            container.containedItemsSystem.addItemToContainer(targetItem, containerAspect);
        };
    }


    @Override
    public String toString() {
        return super.toString() + " put to container " + targetItem.title;
    }
}
