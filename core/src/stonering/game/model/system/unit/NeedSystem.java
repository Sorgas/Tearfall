package stonering.game.model.system.unit;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Objects;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodAspect;
import stonering.entity.unit.aspects.job.TaskAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.need.Need;
import stonering.enums.unit.need.NeedEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameplayConstants;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.TaskContainer;

/**
 * System for handling creatures needs.
 * Increases need states constantly. If some need has reached max value, creates disease for it.
 * Creates mood penalties for needs. Penalties removed when satisfaction action is performed.
 * On update, counts creature needs and creates {@link Task} for strongest need in {@link NeedAspect}.
 * This task is stored in needs aspect (not in {@link TaskContainer}) and considered by {@link CreaturePlanningSystem}.
 *
 * @author Alexander on 22.08.2019.
 */
public class NeedSystem extends EntitySystem<Unit> {

    public NeedSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
        targetAspects.add(NeedAspect.class);
    }

    @Override
    public void update(Unit unit) {
        NeedAspect aspect = unit.get(NeedAspect.class);
        if (aspect == null) return;
        for (Entry<NeedEnum, NeedState> entry : aspect.needs.entrySet()) {
            NeedState state = entry.getValue();
            if (state.current() < state.max) {
                if (state.changeValue(GameplayConstants.DEFAULT_NEED_DELTA)) {
                    Need need = entry.getKey().NEED;
                    if (need.disease != null)
                        GameMvc.model().get(UnitContainer.class).diseaseSystem.addNewDisease(unit, need.disease); // create disease
                    unit.get(MoodAspect.class).addEffect(need.getMoodPenalty(unit, state)); // change mood
                }
            }
        }
        if (aspect.canAcceptTask()) tryAssignNewTask(unit, aspect); // create task
    }

    /**
     * Fetches untolerated needs in the order of their priority, tries to create task for satisfaction.
     * First successfully created task is saved to aspect (and then considered in {@link TaskAspect}).
     */
    private void tryAssignNewTask(Unit unit, NeedAspect aspect) {
        aspect.needs.entrySet().stream()
                .filter(entry -> entry.getKey().NEED.isSatisfied(unit))
                .sorted(Comparator.comparingDouble(entry -> ((Entry<NeedEnum, NeedState>) entry).getValue().current()).reversed())
                .map(entry -> entry.getKey().NEED.tryCreateTask(unit))
                .filter(Objects::nonNull)
                .findFirst().orElse(null); // find first successfully created task
    }
}
