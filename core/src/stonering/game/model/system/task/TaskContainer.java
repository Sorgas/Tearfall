package stonering.game.model.system.task;

import static stonering.enums.action.ActionTargetTypeEnum.EXACT;

import java.util.*;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import stonering.entity.job.Task;
import stonering.entity.job.designation.Designation;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.JobMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.PassageMap;
import stonering.game.model.system.ModelComponent;
import stonering.util.geometry.Position;
import stonering.util.lang.Updatable;
import stonering.util.logging.Logger;

/**
 * Contains all {@link Task} for player's units on map and {@link Designation}s for rendering.
 * Tasks are orders for unit. Tasks are created by player, buildings or zones(farms, storages, workbenches).
 * Tasks for different jobs stored separately.
 * Tasks for units needs stored on each unit's {@link NeedAspect}.
 * Tasks and designations are linked to each other if needed.
 *
 * @author Alexander Kuzyakov
 */
public class TaskContainer implements ModelComponent, Updatable {
    public Map<String, TaskList> tasks; // task job to all tasks with this job
    public final Set<Task> assignedTasks; // tasks, taken by some unit.
    public final HashMap<Position, Designation> designations; //this map is for rendering and modifying designations
    public final DesignationSystem designationSystem;
    public final TaskStatusSystem taskStatusSystem;
    private PassageMap map;

    public TaskContainer() {
        tasks = new HashMap<>();
        JobMap.all().forEach(job -> tasks.put(job.name, new TaskList()));
        assignedTasks = new HashSet<>();
        designations = new HashMap<>();
        designationSystem = new DesignationSystem(this);
        taskStatusSystem = new TaskStatusSystem(this);
    }

    @Override
    public void update(TimeUnitEnum unit) {
        designationSystem.update();
        taskStatusSystem.update();
        tasks.values().forEach(list -> list.update(unit));
    }

    /**
     * Gets tasks for unit. Filters task by units's allowed jobs.
     * Does not assign task to unit, because after this method is compared to unit's other tasks, see {@link stonering.game.model.system.unit.CreaturePlanningSystem}.
     * TODO consider task priority
     */
    public Task getActiveTask(Unit unit) {
        List<String> enabledJobs = new ArrayList<>();
        enabledJobs.add("none");
        unit.optional(JobSkillAspect.class).ifPresent(aspect -> enabledJobs.addAll(aspect.enabledJobs));
        return tasks.keySet().stream()
                .filter(enabledJobs::contains)
                .map(tasks::get)
                .flatMap(taskList -> taskList.tasks.stream())
                .filter(task -> taskTargetReachable(unit, task)) // tasks with reachable targets
                .min(Comparator.comparingInt(task -> task.initialAction.target.getPosition().fastDistance(unit.position))) // nearest target
                .orElse(null);
    }

    /**
     * Moves given task to set of taken tasks, making it unavailable for units to take.
     * If task was generated by unit (e.g. need), does nothing.
     */
    public void claimTask(@NotNull Task task) {
        if (tasks.get(task.job).remove(task)) {
            assignedTasks.add(task);
            Logger.TASKS.logDebug("task " + task + " claimed.");
        } else {
            Logger.TASKS.logError("claimed task " + task + " is not in open tasks.");
        }
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

    /**
     * Adds task to special list for failed tasks, which should be reopened after a delay.
     * Task should be OPEN and have no performer.
     */
    public void reopenTask(@Nonnull Task task) {
        task.reset(); // delete actions
        TaskList list = tasks.get(task.job);
        list.addReopenedTask(task);
        if (task.designation != null) designations.put(task.designation.position, task.designation);
        Logger.TASKS.logDebug(task + "reopened");
    }

    public void removeTask(@Nonnull Task task) {
        if(task.designation != null) designations.remove(task.designation.position);
        assignedTasks.remove(task);
        tasks.get(task.job).remove(task);
    }

    private boolean taskTargetReachable(Unit unit, Task task) {
        return map().util.positionReachable(unit.position, task.initialTarget().getPosition(), task.initialTarget().type != EXACT);
    }

    private PassageMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class).passageMap : map;
    }
}
