package stonering.entity.job.action.target;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.GetItemFromContainerAction;
import stonering.entity.job.action.ItemPickupAction;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.NEW;

/**
 * Fictive action to handle different cases of item placement.
 * Checks item placement an creates corresponding pre-action.
 * Does not check item availability or performer condition. // TODO change this to speed up checking?
 * Does not check its own progress, only if target item is equipped to performer.
 *
 * @author Alexander on 04.02.2020.
 */
public class ObtainItemAction extends Action {

    public ObtainItemAction(Item item) {
        super(new EntityActionTarget(item, ActionTargetTypeEnum.ANY));
        startCondition = () -> {
            ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
            if(itemContainer.containedItemsSystem.itemIsContained(item)) { // take from container
                Entity container = itemContainer.contained.get(item).entity;
                task.addFirstPreAction(new GetItemFromContainerAction(item, container));
            } else if(itemContainer.equippedItemsSystem.isItemEquipped(item)) {
                return FAIL;
                //TODO
            } else { // pickup from ground
                task.addFirstPreAction(new ItemPickupAction(item));
            }
            return NEW;
        };
        finishCondition = () -> task.performer.getAspect(EquipmentAspect.class).hauledItems.contains(item);
    }
}
