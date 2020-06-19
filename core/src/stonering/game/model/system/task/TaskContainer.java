package stonering.game.model.system.task;

import org.jetbrains.annotations.NotNull;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.designation.Designation;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.action.TaskStatusEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.JobsEnum;
import stonering.game.GameMvc;
import stonering.util.global.Updatable;
import stonering.game.model.local_map.passage.PassageMap;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.job.Task;
import stonering.util.logging.Logger;

import java.util.*;

/**
 * Contains all {@link Task} for player's units on map and {@link Designation}s for rendering.
 * Tasks are orders for unit. Tasks are created by player, buildings or zones(farms, storages, workbenches).
 * Tasks for units needs stored on each unit's {@link NeedsAspect}.
 * Tasks and designations are linked to each other if needed.
 *
 * @author Alexander Kuzyakov
 */
public class TaskContainer implements ModelComponent, Updatable {
    public Map<JobsEnum, LinkedList<Task>> tasks; // task job to all tasks with this job
    public final Set<Task> assignedTasks; // tasks, taken by some unit.
    public final HashMap<Position, Designation> designations; //this map is for rendering and modifying designations
    public final DesignationSystem designationSystem;
    public final TaskStatusSystem taskStatusSystem;

    public TaskContainer() {
        tasks = new HashMap<>();
        Arrays.stream(JobsEnum.values()).forEach(value -> tasks.put(value, new LinkedList<>()));
        assignedTasks = new HashSet<>();
        designations = new HashMap<>();
        designationSystem = new DesignationSystem(this);
        taskStatusSystem = new TaskStatusSystem(this);
    }

    @Override
    public void update(TimeUnitEnum unit) {
        designationSystem.update();
        taskStatusSystem.update();
    }

    /**
     * Gets tasks for unit. Filters task by units's allowed jobs.
     * Does not assign task to unit, because after this method is compared to unit's other tasks, see {@link stonering.game.model.system.unit.CreaturePlanningSystem}.
     */
    public Task getActiveTask(Unit unit) {
        // TODO consider task priority
        JobsAspect aspect = unit.get(JobsAspect.class);
        if (aspect == null)
            return Logger.TASKS.logError("Creature " + unit + " without jobs aspect gets task from container", null);
        PassageMap map = GameMvc.model().get(LocalMap.class).passageMap;
        for (JobsEnum enabledJob : aspect.getEnabledJobs()) {
            for (Task task : tasks.get(enabledJob)) {
                if (task.performer != null) Logger.TASKS.logError("Task " + task + " with performer is in open map.");
                ActionTarget target = task.nextAction.target;
                if (task.performer == null &&
                        task.status == TaskStatusEnum.OPEN &&
                        map.util.positionReachable(unit.position, target.getPosition(), target.targetType != ActionTargetTypeEnum.EXACT)) {
                    //TODO add selecting nearest task.
                    return task;
                }
            }
        }
        return null;
    }

    /**
     * Moves given task to set of taken tasks, making it unavailable for units to take.
     * If task was generated by unit (e.g. need), does nothing.
     */
    public void claimTask(@NotNull Task task) {
        if (tasks.containsKey(task.job) && tasks.get(task.job).remove(task)) assignedTasks.add(task);
    }

    public void addTask(Task task) {
        if (task == null) return;
        LinkedList<Task> list = tasks.get(task.job);
        int index = 0;
        if(!list.isEmpty()) { // index for task insertion is based on priority
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).priority <= task.priority) {
                    index = i;
                    break;
                }
            }
        }
        list.add(index, task);
        if (task.designation != null) designations.put(task.designation.position, task.designation);
        Logger.TASKS.logDebug("Task " + task + " added to TaskContainer.");
    }
}
