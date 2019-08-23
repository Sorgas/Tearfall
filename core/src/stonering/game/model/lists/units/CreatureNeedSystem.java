package stonering.game.model.lists.units;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.NeedAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.aspects.needs.Need;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.util.global.Pair;

/**
 * System for generation needs satisfying tasks for units.
 * Works in {@link UnitContainer}.
 * On update, counts creature needs and creates {@link Task} in {@link NeedAspect}.
 * This task is then considered by {@link PlanningAspect}.
 *
 * @author Alexander on 22.08.2019.
 */
public class CreatureNeedSystem {

    public void updateNeedForCreature(Unit unit) {
        NeedAspect aspect = unit.getAspect(NeedAspect.class);
        if(aspect == null) return;
        Pair<Need, Integer> strongestNeed = getNeedWithMaxPriority(unit, aspect);
        if(strongestNeed.getValue() < 0) aspect.satisfyingTask = null; // need can be tolerated.
        aspect.satisfyingTask = strongestNeed.getKey().tryCreateTask(unit);
    }

    /**
     * Selects need with maximum priority,
     */
    private Pair<Need, Integer> getNeedWithMaxPriority(Unit unit, NeedAspect aspect) {
        NeedEnum strongestNeed = null;
        int maxPriority = 0;
        int priority = 0;
        for (NeedEnum need : aspect.needs) {
            if(strongestNeed == null || maxPriority < (priority = need.need.countPriority(unit))) {
                strongestNeed = need;
                maxPriority = priority;
            }
        }
        return new Pair<>(strongestNeed.need, maxPriority);
    }
}
