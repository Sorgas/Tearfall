package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.global.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static stonering.enums.action.TaskPriorityEnum.NONE;
import static stonering.enums.action.TaskStatusEnum.COMPLETE;
import static stonering.enums.action.TaskStatusEnum.FAILED;

/**
 * System for generation need satisfying tasks for units.
 * Works in {@link UnitContainer}.
 * On update, counts creature needs and creates {@link Task} for strongest need in {@link NeedsAspect}.
 * This task is stored in needs aspect and considered by {@link CreaturePlanningSystem}.
 *
 * @author Alexander on 22.08.2019.
 */
public class CreatureNeedSystem extends EntitySystem<Unit> {

    public CreatureNeedSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
        targetAspects.add(NeedsAspect.class);
    }

    /**
     * Fetches untolerated needs in the order of their priority, tries to create task fo satisfaction.
     * First successfully created task is saved to aspect (and then considered in {@link TaskAspect}).
     */
    @Override
    public void update(Unit unit) {
        NeedsAspect aspect = unit.get(NeedsAspect.class);
        if(clearCompletedTask(aspect)) tryAssignNewTask(unit, aspect);
    }

    private boolean clearCompletedTask(NeedsAspect aspect) {
        if (aspect.satisfyingTask != null && (aspect.satisfyingTask.status == FAILED || aspect.satisfyingTask.status == COMPLETE))
            aspect.satisfyingTask = null; // remove ended task from aspect
        return aspect.satisfyingTask == null;
    }

    private void tryAssignNewTask(Unit unit, NeedsAspect aspect) {
        List<Pair<NeedEnum, Integer>> needs = getUntoleratedNeeds(unit, aspect);
        needs.sort(Comparator.comparingInt(Pair::getValue));
        for (Pair<NeedEnum, Integer> need : needs) {
            aspect.satisfyingTask = need.getKey().NEED.tryCreateTask(unit);
            if (aspect.satisfyingTask != null) return;
        }
    }

    private List<Pair<NeedEnum, Integer>> getUntoleratedNeeds(Unit unit, NeedsAspect aspect) {
        return aspect.needs.stream().map(need -> new Pair<>(need, need.NEED.countPriority(unit).VALUE))
                .filter(pair -> pair.value >= NONE.VALUE).collect(Collectors.toList());
    }
}
