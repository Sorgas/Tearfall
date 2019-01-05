package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.target.BuildingTargetAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;

/**
 * @author Alexander on 04.01.2019.
 */
public class PutItemFromInventoryToContainerEffectAspect extends EffectAspect {
    private Item item;

    public PutItemFromInventoryToContainerEffectAspect(Action action, int workAmount, Item item) {
        super(action, workAmount);
        this.item = item;
    }

    @Override
    protected void applyEffect() {
        ItemContainerAspect itemContainerAspect = (ItemContainerAspect) ((BuildingTargetAspect) action.getTargetAspect()).getBuilding().getAspects().get(ItemContainerAspect.NAME);
        itemContainerAspect.getItems().add(item);
    }
}
