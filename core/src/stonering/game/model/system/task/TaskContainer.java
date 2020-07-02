package stonering.game.model.system.task;

import org.jetbrains.annotations.NotNull;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.designation.Designation;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.JobMap;
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
 * Tasks for different jobs stored separately.
 * Tasks for units needs stored on each unit's {@link NeedsAspect}.
 * Tasks and designations are linked to each other if needed.
 *
 * @author Alexander Kuzyakov
 */
public class TaskContainer implements ModelComponent, Updatable {
    public Map<JobMap, TaskList> tasks; // task job to all tasks with this job
    public final Set<Task> assignedTasks; // tasks, taken by some unit.
    public final HashMap<Position, Designation> designations; //this map is for rendering and modifying designations
    public final DesignationSystem designationSystem;
    public final TaskStatusSystem taskStatusSystem;

    public TaskContainer() {
        tasks = new HashMap<>();
        Arrays.stream(JobMap.values()).forEach(value -> tasks.put(value, new TaskList()));
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
        for (JobMap enabledJob : aspect.enabledJobs) {
            for (Task task : tasks.get(enabledJob).tasks) {
                ActionTarget target = task.nextAction.target;
                if (map.util.positionReachable(unit.position, target.getPosition(), target.targetType != ActionTargetTypeEnum.EXACT)) {
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
        if (tasks.get(task.job).remove(task)) assignedTasks.add(task);
    }

    public void addTask(Task task) {
        Optional.ofNullable(task)
                .map(task1 -> tasks.get(task1.job))
                .ifPresent(list -> {
                    list.add(task);
                    if (task.designation != null) designations.put(task.designation.position, task.designation);
                    Logger.TASKS.logDebug("Task " + task + " added to TaskContainer.");
                });
    }

    public void addReopenedTask(Task task) {
        Optional.ofNullable(task)
                .map(task1 -> tasks.get(task1.job))
                .ifPresent(list -> {
                    list.addReopenedTask(task);
                    if (task.designation != null) designations.put(task.designation.position, task.designation);
                    Logger.TASKS.logDebug("Reopened task " + task + " added to TaskContainer.");
                });
    }

    public boolean removeTask(Task task) {
        return assignedTasks.remove(task) || tasks.get(task.job).remove(task);
    }
}
