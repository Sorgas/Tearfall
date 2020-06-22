package stonering.game.model.system.task;

import stonering.entity.building.BuildingOrder;
import stonering.entity.job.Task;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.designation.OrderDesignation;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import static stonering.enums.action.TaskStatusEnum.*;

import java.util.Optional;

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
        container.designations.values().forEach(designation -> {
                    if (designation.task == null) createTaskForDesignation(designation);
                    if (designation instanceof BuildingDesignation) {
                        if (!((BuildingDesignation) designation).checkSite()) {
                            Logger.BUILDING.logWarn("Place for building became invalid.");
                            designation.task.status = CANCELED;
                        }
                    }
                }
        );
    }

    private void createTaskForDesignation(Designation designation) {
        container.addTask(taskCreator.createTaskForDesignation(designation, 1));
    }

    /**
     * Validates designation and creates comprehensive task.
     * All simple orders like digging and foraging submitted through this method.
     */
    public void submitDesignation(Position position, DesignationTypeEnum type) {
        removeDesignation(position); // remove previous designation
        if (type != DesignationTypeEnum.D_NONE && type.VALIDATOR.apply(position))
            container.designations.put(position, new OrderDesignation(position, type));
    }

    public void removeDesignation(Designation designation) {
        Optional.ofNullable(container.designations.get(designation.position)) // cancel previous designation
                .filter(foundDesignation -> foundDesignation == designation) // designation still present on map
                .map(foundDesignation -> foundDesignation.task)
                .ifPresent(task -> task.status = CANCELED);
    }

    private void removeDesignation(Position position) {
        Optional.ofNullable(container.designations.get(position)) // cancel previous designation
                .map(foundDesignation -> foundDesignation.task)
                .ifPresent(task -> task.status = CANCELED);
    }

    /**
     * Adds designation and creates comprehensive task.
     * All single-tile buildings are constructed through this method.
     */
    public void submitBuildingDesignation(BuildingOrder order, int priority) {
        System.out.println("submitting designation");
        if (!PlaceValidatorsEnum.getValidator(order.blueprint.placing).apply(order.position)) return;
        System.out.println("validation passed");
        BuildingDesignation designation = new BuildingDesignation(order);
        Task task = taskCreator.createBuildingTask(designation, priority);
        designation.task = task;
        container.addTask(task);
        container.designations.put(designation.position, designation);
        Logger.TASKS.log(task.name + " designated");
    }
}
