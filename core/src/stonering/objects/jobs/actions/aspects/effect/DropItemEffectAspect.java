package stonering.objects.jobs.actions.aspects.effect;

import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * @author Alexander on 08.07.2018.
 */
public class DropItemEffectAspect extends EffectAspect {
    private Item item;

    public DropItemEffectAspect(Action action, Item item) {
        super(action, 10);
        this.item = item;
    }

    @Override
    protected void applyEffect() {
        EquipmentAspect equipmentAspect = (EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment");
        if (equipmentAspect != null && equipmentAspect.checkItem(item)) {
            equipmentAspect.unequipItem(item);
        }
        action.getGameContainer().getItemContainer().putItem(item, action.getPerformer().getPosition());
    }
}
