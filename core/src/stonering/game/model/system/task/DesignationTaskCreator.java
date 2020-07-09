package stonering.game.model.system.task;

import java.util.Optional;

import stonering.entity.job.Task;
import stonering.entity.job.action.*;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.plant.PlantBlock;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.logging.Logger;

/**
 * Factory class for creating {@link Task}s based on {@link Designation}s.
 *
 * @author Alexander on 16.10.2019.
 */
public class DesignationTaskCreator {

    public Task createTaskForDesignation(Designation designation, int priority) {
        if(designation instanceof OrderDesignation) {
            return Optional.ofNullable(selectActionByDesignation((OrderDesignation) designation))
                    .map(action -> createTask(action, designation, priority))
                    .orElse(null);
        } else if(designation instanceof BuildingDesignation) {
            return createBuildingTask((BuildingDesignation) designation, priority);
        } else {
            Logger.TASKS.logError("Cannot create Task for unsupported designation type.");
            return null;
        }
    }

    private Action selectActionByDesignation(OrderDesignation designation) {
        switch (designation.type) {
            case D_DIG:
            case D_RAMP:
            case D_STAIRS:
            case D_CHANNEL:
                return new DigAction(designation);
            case D_CUT:
            case D_CHOP:
                //TODO split actions
                return new ChopTreeAction(designation);
            case D_HARVEST:
                //TODO probably create multiple tasks for all tree blocks
                PlantBlock block = GameMvc.model().get(PlantContainer.class).getPlantBlock(designation.position);
                //TODO add product aspect to plants
//                if (block.getPlant().isHarvestable()) action = new PlantHarvestAction(block.getPlant());
                return null;
            default:
                return null;
        }
    }

    /**
     * Creates tasks for building various buildings.
     */
    public Task createBuildingTask(BuildingDesignation designation, int priority) {
        Action action;
        if (designation.order.blueprint.construction) {
            action = new ConstructionAction(designation.order);
        } else {
            action = new BuildingAction(designation.order);
        }
        return createTask(action, designation, priority);
    }

    private Task createTask(Action action, Designation designation, int priority) {
        Task task = new Task(action, designation.type.JOB);
        task.designation = designation;
        designation.task = task;
        return task;
    }
}