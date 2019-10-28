package stonering.game.model.system.tasks;

import org.jetbrains.annotations.NotNull;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.building.BuildingOrder;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.JobsAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.job.Task;
import stonering.util.global.Logger;

import java.util.*;

import static stonering.enums.TaskStatusEnum.OPEN;

/**
 * Contains all tasks for settlers on map and Designations for rendering.
 * Tasks are created by player or by buildings and zones(farms, storages, workbenches).
 * <p>
 * {@link Task} are orders for unit.
 * {@link Designation} are used for drawing given orders as tiles.
 * Tasks and designations are linked to each other if needed.
 *
 * @author Alexander Kuzyakov
 */
public class TaskContainer implements ModelComponent {
    private Map<String, List<Task>> tasks; // task job to all tasks with this job
    public final HashMap<Position, Designation> designations; //this map is for rendering and modifying designations
    public final Set<Task> assignedTasks;
    public final DesignationsValidator validator;
    private TaskCreator taskCreator;
    private Position cachePosition; // state is not maintained. should be set before use

    public TaskContainer() {
        tasks = new HashMap<>();
        assignedTasks = new HashSet<>();
        designations = new HashMap<>();
        validator = new DesignationsValidator();
        taskCreator = new TaskCreator();
        cachePosition = new Position();
    }

    /**
     * Gets tasks for unit. Filters task by units's allowed jobs.
     * Does not assign task to unit, because after this method is compared to unit's other tasks, see {@link PlanningAspect}.
     */
    public Task getActiveTask(Unit unit) {
        // TODO ocnsider task priority
        JobsAspect aspect = unit.getAspect(JobsAspect.class);
        if (aspect == null) {
            Logger.TASKS.logError("Creature " + unit + " without jobs aspect gets task from container");
            return null;
        }
        final Position position = unit.position;
        for (String enabledJob : aspect.getEnabledJobs()) {
            if (!tasks.containsKey(enabledJob)) continue;
            for (Task task : tasks.get(enabledJob)) {
                if(task.performer != null) Logger.TASKS.logError("Task " + task + " with performer is in open map." );
                GameMvc.instance().getModel().get(LocalMap.class).getPassage().hasPathBetween(position, task.nextAction.actionTarget.getPosition());
                if (task.performer == null && task.isTaskTargetsAvailableFrom(position) && task.status == OPEN) {
                    //TODO add selecting nearest task.
                    return task;
                }
            }
        }
        return null;
    }

    /**
     * Moves given task to set of taken tasks, making it unavailable for units to take.
     */
    public boolean claimTask(@NotNull Task task) {
        if (!tasks.get(task.job).remove(task))
            return Logger.TASKS.logError("Assigned task " + task + " not present in container, or has invalid job: " + task.job, false);
        if (!assignedTasks.add(task))
            return Logger.TASKS.logError("Assigned task " + task + " is already assigned in container. Performer: " + task.performer, false);
        return true;
    }

    /**
     * Validates designation and creates comprehensive task.
     * All simple orders like digging and foraging submitted through this method.
     */
    public Task submitDesignation(Position position, DesignationTypeEnum type, int priority) {
        if (!validator.validateDesignation(position, type)) return null; // no designation for invalid position
        OrderDesignation designation = new OrderDesignation(position, type);
        return addTask(taskCreator.createOrderTask(designation, priority));
    }

    /**
     * Called from {@link BuildingDesignationSequence}.
     * Adds designation and creates comprehensive task.
     * All single-tile buildings are constructed through this method.
     */
    public void submitBuildingDesignation(BuildingOrder order, int priority) {
        Position position = order.getPosition();
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        if (!PlaceValidatorsEnum.getValidator(order.getBlueprint().placing).validate(localMap, position)) return;
        BuildingDesignation designation = new BuildingDesignation(position, order.getBlueprint().building);
        Task task = taskCreator.createBuildingTask(designation, order.getItemSelectors().values(), priority);

        addTask(task);
        designations.put(designation.position, designation);
        Logger.TASKS.log(task.name + " designated");
    }

    /**
     * Removes task. called if task is finished or canceled.
     * Removes tasks designation if there is one.
     */
    public void removeTask(Task task) {
        tasks.get(task.job).remove(task);
        if (task.designation != null) designations.remove(task.designation.position);
    }

    /**
     * For adding simple tasks (w/o designation).
     */
    public Task addTask(Task task) {
        if (task == null) return null;
        tasks.putIfAbsent(task.job, new ArrayList<>()); // new list for job
        tasks.get(task.job).add(task);
        if (task.designation != null) designations.put(task.designation.position, task.designation);
        Logger.TASKS.logDebug("Task " + task + " added to TaskContainer.");
        return task;
    }

    public Designation getDesignation(int x, int y, int z) {
        return designations.get(cachePosition.set(x, y, z));
    }

    /**
     * Removes task from container,
     */
    public void finishTask(Task task) {

    }

    public void freeTaskLocks(Task task) {
        task.lockedItems.
    }
}
