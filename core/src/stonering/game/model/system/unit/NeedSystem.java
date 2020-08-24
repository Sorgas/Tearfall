package stonering.game.model.system.unit;

import static stonering.enums.action.TaskPriorityEnum.NONE;

import java.util.Comparator;
import java.util.Objects;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.entity.unit.aspects.body.DiseaseState;
import stonering.enums.unit.need.Need;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.health.disease.DiseaseMap;
import stonering.game.GameMvc;
import stonering.game.model.GamePlayConstants;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.TaskContainer;

/**
 * System for handling creatures needs.
 * On update, counts creature needs and creates {@link Task} for strongest need in {@link NeedAspect}.
 * This task is stored in needs aspect (not in {@link TaskContainer}) and considered by {@link CreaturePlanningSystem}.
 * If some need has reached max value, creates disease for it.
 * Creates mood penalties for needs. Penalties removed when satisfaction action is performed.
 *
 * @author Alexander on 22.08.2019.
 */
public class NeedSystem extends EntitySystem<Unit> {

    public NeedSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
        targetAspects.add(NeedAspect.class);
    }

    /**
     * Checks if current needs task is ended, and creates a new one if needed.
     */
    @Override
    public void update(Unit unit) {
        NeedAspect aspect = unit.get(NeedAspect.class);
        if (aspect == null) return;
        // roll state
        for (NeedState state : aspect.needs.values()) {
            Need need = state.need.NEED;
            if (state.current() < state.max) {
                if (state.changeValue(GamePlayConstants.DEFAULT_NEED_DELTA)) {
                    if(need.relatedDisease != null) addDisease(unit, need); // create disease
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
        aspect.satisfyingTask = aspect.needs.values().stream()
                .filter(state -> state.current() > NONE.NEED_THRESHOLD) // filter tolerated needs
                .sorted(Comparator.comparingDouble(state -> ((NeedState) state).current()).reversed()) // sort by priority
                .map(state -> state.need.NEED.tryCreateTask(unit))
                .filter(Objects::nonNull)
                .findFirst().orElse(null); // find first successfully created task
    }

    private void addDisease(Unit unit, Need need) {
        GameMvc.model().get(UnitContainer.class).creatureDiseaseSystem
                .addNewDisease(unit, new DiseaseState(DiseaseMap.get(need.relatedDisease)));
    }
}
