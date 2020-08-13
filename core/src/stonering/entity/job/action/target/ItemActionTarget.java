package stonering.entity.job.action.target;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.READY;

/**
 * Targets action to item, unit should reach item exact position to act.
 * Also, item counted reachable, if is is hauled by action performer.
 *
 * @author Alexander Kuzyakov on 28.01.2018.
 */
public class ItemActionTarget extends EntityActionTarget {
    public Item item;

    public ItemActionTarget(Item item) {
        super(item, ActionTargetTypeEnum.EXACT);
        this.item = item;
    }

    @Override
    public ActionTargetStatusEnum check(Entity performer) {
        if (performer.optional(EquipmentAspect.class).map(aspect -> aspect.items.contains(item)).orElse(false))
            return READY; // item is in hands already
        return super.check(performer);
    }
}
