package stonering.game.model.system.task;

import stonering.entity.building.BuildingOrder;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.Task;
import stonering.entity.job.action.*;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.plants.PlantBlock;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.system.PlantContainer;

import java.util.Collection;

/**
 * Factory class for creating {@link Task}s based on {@link Designation}s.
 * @author Alexander on 16.10.2019.
 */
public class TaskCreator {

    /**
     * Creates tasks for simple orders, basing on designation type.
     */
    public Task createOrderTask(OrderDesignation designation, int priority) {
        Action action = null;
        switch (designation.getType()) {
            case NONE:
                return null;
            case DIG:
            case RAMP:
            case STAIRS:
            case CHANNEL: {
                action = new DigAction(designation);
                break;
            }
            case CUT:
            case CHOP: {
                //TODO split actions
                action = new ChopTreeAction(designation);
                break;
            }
            case HARVEST: {
                //TODO probably create multiple tasks for all tree blocks
                PlantBlock block = GameMvc.instance().model().get(PlantContainer.class).getPlantBlock(designation.position);
                if (block.getPlant().isHarvestable()) action = new PlantHarvestAction(block.getPlant());
                break;
            }
        }
        return action != null ? createTask(action, designation, priority) : null;

    }

    /**
     * Creates tasks for building various buildings.
     */
    public Task createBuildingTask(BuildingDesignation designation, int priority) {
        Action action;
        if (BuildingTypeMap.instance().getBuilding(designation.order.blueprint.building).construction) {
            action = new ConstructionAction(designation.order);
        } else {
            action = new BuildingAction(designation.order);
        }
        return createTask(action, designation, priority);
    }

    private Task createTask(Action action, Designation designation, int priority) {
        Task task = new Task("designation", action, priority);
        task.designation = designation;
        designation.task = task;
        return task;
    }
}
