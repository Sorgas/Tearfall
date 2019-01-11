package stonering.entity.jobs.actions.aspects;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.items.Item;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;

/**
 * Action for picking and hauling item. Performer should have {@link EquipmentAspect}
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickAction extends Action {
    private Item targetItem;

    public ItemPickAction(GameContainer gameContainer, Item targetItem) {
        super(gameContainer);
        this.targetItem = targetItem;
    }

    @Override
    protected boolean doLogic() {
        ((EquipmentAspect) task.getPerformer().getAspects().get(EquipmentAspect.NAME)).pickupItem(targetItem);
        return true;
    }

    @Override
    public boolean check() {
        if (!task.getPerformer().getAspects().containsKey(EquipmentAspect.NAME)) return false;
        return gameContainer.getItemContainer().checkItem(targetItem);
    }

    @Override
    public Position getTargetPosition() {
        return targetItem.getPosition();
    }
}
