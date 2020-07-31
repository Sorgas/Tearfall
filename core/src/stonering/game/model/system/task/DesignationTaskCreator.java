package stonering.game.model.system.task;

import java.util.Optional;

import stonering.entity.job.Task;
import stonering.entity.job.action.*;
import stonering.entity.job.action.plant.*;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.designation.PlantingDesignation;
import stonering.entity.plant.Plant;
import stonering.enums.designations.PlaceValidatorEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;

/**
 * Factory class for creating {@link Task}s based on {@link Designation}s.
 *
 * @author Alexander on 16.10.2019.
 */
public class DesignationTaskCreator {

    public Task createTaskForDesignation(Designation designation, int priority) {
        return Optional.ofNullable(designation)
                .map(this::selectActionByDesignation)
                .map(action -> new Task(action, designation.type.JOB))
                .map(task -> bindTask(task, designation)).orElse(null);
    }

    private Action selectActionByDesignation(Designation designation) {
        if (designation instanceof BuildingDesignation) {
            return createBuildingAction((BuildingDesignation) designation);
        } else if (designation instanceof PlantingDesignation) {
            return createPlantingAction((PlantingDesignation) designation);
        } else {
            switch (designation.type) {
                case D_DOWNSTAIRS:
                case D_DIG:
                case D_RAMP:
                case D_STAIRS:
                case D_CHANNEL:
                    return new DigAction(designation);
                case D_CUT:
                case D_CUT_FARM:
                    return GameMvc.model().optional(PlantContainer.class)
                            .map(container -> container.getPlantInPosition(designation.position))
                            .filter(plant -> plant instanceof Plant)
                            .map(plant -> new CutPlantAction((Plant) plant))
                            .orElse(null);
                case D_CHOP:
                    return new ChopTreeAction(designation); //TODO split actions
                case D_HARVEST:
                    return GameMvc.model().optional(PlantContainer.class)
                            .map(container -> container.getPlantBlock(designation.position))
                            .map(block -> new PlantHarvestAction(block.plant))
                            .orElse(null);
                case D_HOE:
                    return new HoeingAction(designation);
            }
        }
        return null;
    }

    public Action createBuildingAction(BuildingDesignation designation) {
        return designation.order.blueprint.construction
                ? new ConstructionAction(designation.order)
                : new BuildingAction(designation.order);
    }

    private Action createPlantingAction(PlantingDesignation designation) {
        return Optional.ofNullable(designation)
                .filter(des -> PlaceValidatorEnum.FARM.VALIDATOR.apply(des.position))
                .map(PlantingAction::new)
                .orElse(null);
    }
    
    private Task bindTask(Task task, Designation designation) {
        task.designation = designation;
        designation.task = task;
        return task;
    }
}
