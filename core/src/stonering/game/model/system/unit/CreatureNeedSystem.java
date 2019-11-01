package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.util.global.Pair;

import java.util.*;

/**
 * System for generation needs satisfying tasks for units.
 * Works in {@link UnitContainer}.
 * On update, counts creature needs and creates {@link Task} in {@link NeedsAspect}.
 * This task is then considered by {@link PlanningAspect}.
 *
 * @author Alexander on 22.08.2019.
 */
public class CreatureNeedSystem {

    /**
     * Fetches untolerated needs in the order of their priority, tries to create task fo satisfaction.
     * First successfully created task is saved to aspect (and then considered in {@link PlanningAspect}).
     */
    public void updateNeedForCreature(Unit unit) {
        NeedsAspect aspect = unit.getAspect(NeedsAspect.class);
        if(aspect == null || aspect.satisfyingTask != null) return; // creature has no needs, or already has a task for a need.
        List<Pair<NeedEnum, Integer>> needs = getUntoleratedNeeds(unit, aspect);
        if(needs.size() > 1) needs.sort(Comparator.comparingInt(Pair::getValue));
        for (Pair<NeedEnum, Integer> need : needs) {
            aspect.satisfyingTask = need.getKey().need.tryCreateTask(unit);
            if(aspect.satisfyingTask != null) return;
        }
    }

    /**
     * Collects creature needs that cannot be tolerated. Returns them ordered by priorities.
     */
    private List<Pair<NeedEnum, Integer>> getUntoleratedNeeds(Unit unit, NeedsAspect aspect) {
        int priority;
        List<Pair<NeedEnum, Integer>> list = new ArrayList<>();
        for (NeedEnum need : aspect.needs) {
            if((priority = need.need.countPriority(unit).VALUE) < 0) continue; // skip tolerated need
            list.add(new Pair<>(need, priority));
        }
        return list;
    }
}
