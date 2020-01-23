package stonering.game.model.system.task;

import stonering.entity.building.BuildingOrder;
import stonering.entity.job.Task;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.designation.OrderDesignation;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import static stonering.enums.action.TaskStatusEnum.FAILED;

/**
 * Creates tasks for designations, recreates failed tasks.
 * When {@link Designation}'s {@link Task} is failed, it is removed from {@link TaskContainer},
 * but designation remains in container with failed task, in order to check designation state.
 * TODO add delay for failed task recreation.
 *
 * @author Alexander on 01.11.2019.
 */
public class DesignationSystem {
    private TaskContainer container;
    private TaskCreator taskCreator;

    public DesignationSystem(TaskContainer container) {
        this.container = container;
        taskCreator = new TaskCreator();
    }

    public void update() {
        container.designations.values().forEach(
                designation -> {
                    if (designation.task == null) createTaskForDesignation(designation);
                }
        );
    }

    private void createTaskForDesignation(Designation designation) {
        Logger.TASKS.logError("write some code"); //TODO
    }

    /**
     * Validates designation and creates comprehensive task.
     * All simple orders like digging and foraging submitted through this method.
     */
    public Task submitDesignation(Position position, DesignationTypeEnum type, int priority) {
        if (!type.validator.validate(position)) return null; // no designation for invalid position
        OrderDesignation designation = new OrderDesignation(position, type);
        return container.addTask(taskCreator.createOrderTask(designation, priority));
    }

    /**
     * Called from {@link BuildingDesignationSequence}.
     * Adds designation and creates comprehensive task.
     * All single-tile buildings are constructed through this method.
     */
    public void submitBuildingDesignation(BuildingOrder order, int priority) {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        if (!PlaceValidatorsEnum.getValidator(order.blueprint.placing).validate(order.position)) return;
        BuildingDesignation designation = new BuildingDesignation(order);
        Task task = taskCreator.createBuildingTask(designation, priority);
        container.addTask(task);
        container.designations.put(designation.position, designation);
        Logger.TASKS.log(task.name + " designated");
    }

    public void removeDesignation(Designation designation) {
        container.designations.remove(designation.position); // remove designation to not render it
        if (designation.task != null) designation.task.status = FAILED; // task will be removed by tasksStatusSystem
    }
}
