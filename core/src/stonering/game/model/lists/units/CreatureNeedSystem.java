package stonering.game.model.lists.units;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.NeedAspect;
import stonering.entity.unit.aspects.needs.Need;
import stonering.entity.unit.aspects.needs.NeedEnum;

/**
 * System for generation needs satisfying tasks for units.
 * Works in {@link UnitContainer}.
 *
 * @author Alexander on 22.08.2019.
 */
public class CreatureNeedSystem {

    public void updateNeedForCreature(Unit unit) {
        if(!unit.hasAspect(NeedAspect.class)) return;
        for (NeedEnum need : unit.getAspect(NeedAspect.class).needs) {
            need.need.
        }
    }

    private Need getNeedWithMaxPriority(Unit unit, NeedAspect aspect) {
        NeedEnum strongestNeed = null;
        int strongestPriority = 0;
        int priority = 0;
        for (NeedEnum need : aspect.needs) {
            if(strongestNeed == null || strongestPriority < (priority = need.need.countPriority(unit))) {
                strongestNeed = need;
                strongestPriority = priority;
            }
        }
        return strongestNeed.need;
    }
}
