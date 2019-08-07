package stonering.game.model.lists.tasks;

import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.job.action.*;
import stonering.entity.building.BuildingOrder;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.game.model.lists.ModelComponent;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.job.Task;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.plants.PlantBlock;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Contains all tasks for settlers on map and Designations for rendering.
 * {@link Task} are orders for unit.
 * {@link Designation} are used for drawing given orders as tiles.
 *
 * @author Alexander Kuzyakov
 */
public class TaskContainer implements ModelComponent {
    private ArrayList<Task> tasks;
    private HashMap<Position, Designation> designations;
    private Position cachePosition; // state is not maintained. should be set before use
    private DesignationsValidator designationsValidator;

    public TaskContainer() {
        cachePosition = new Position(0, 0, 0);
        tasks = new ArrayList<>();
        designations = new HashMap<>();
        designationsValidator = new DesignationsValidator();
    }

    /**
     * Gets tasks for unit.
     * //TODO
     */
    public Task getActiveTask(Position pos) {
        for (Task task : tasks) {
            if (task.getPerformer() == null && task.isTaskTargetsAvailableFrom(pos)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Adds designation and creates comprehensive task.
     * All simple orders like digging and foraging submitted through this method.
     */
    public Task submitOrderDesignation(Position position, DesignationTypeEnum type, int priority) {
        if (!designationsValidator.validateDesignations(position, type)) return null; // no designation for invalid position
        OrderDesignation designation = new OrderDesignation(position, type);
        Task task = createOrderTask(designation, priority);
        if (task == null) return null; // no designation with no task
        Logger.TASKS.logDebug("designation " + type + " submitted");
        task.setDesignation(designation);
        designation.setTask(task);
        tasks.add(task);
        designations.put(designation.getPosition(), designation);
        Logger.TASKS.log(task.getName() + " designated");
        Logger.TASKS.logDebug("tasks number: " + tasks.size());
        Logger.TASKS.logDebug("designation number: " + designations.size());
        return task;
    }

    /**
     * Called from {@link BuildingDesignationSequence}.
     * Adds designation and creates comprehensive task.
     * All single-tile buildings are constructed through this method.
     */
    public void submitBuildingDesignation(BuildingOrder order, int priority) {
        Position position = order.getPosition();
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        if (!PlaceValidatorsEnum.getValidator(order.getBlueprint().getPlacing()).validate(localMap, position)) return;
        BuildingDesignation designation = new BuildingDesignation(position, DesignationTypeEnum.BUILD, order.getBlueprint().getBuilding());
        Task task = createBuildingTask(designation, order.getItemSelectors().values(), priority);
        designation.setTask(task);
        tasks.add(task);
        designations.put(designation.getPosition(), designation);
        Logger.TASKS.log(task.getName() + " designated");
    }

    private Task createOrderTask(OrderDesignation designation, int priority) {
        switch (designation.getType()) {
            case NONE:
                break;
            case DIG:
            case RAMP:
            case STAIRS:
            case CHANNEL: {
                DigAction digAction = new DigAction(designation);
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, digAction, priority);
                return task;
            }
            //TODO split actions
            case CUT:
            case CHOP: {
                ChopTreeAction chopTreeAction = new ChopTreeAction(designation);
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, chopTreeAction, priority);
                return task;
            }
            case HARVEST: {
                PlantBlock block = GameMvc.instance().getModel().get(PlantContainer.class).getPlantBlock(designation.getPosition());
                if (!block.getPlant().isHarvestable()) return null;
                PlantHarvestAction plantHarvestAction = new PlantHarvestAction(block.getPlant());
                //TODO probably create multiple tasks for all blocks
                return new Task("designation", TaskTypesEnum.DESIGNATION, plantHarvestAction, priority);
            }
        }
        return null;
    }

    /**
     * Creates tasks for building various buildings.
     */
    private Task createBuildingTask(BuildingDesignation designation, Collection<ItemSelector> itemSelectors, int priority) {
        Action action;
        if (BuildingTypeMap.getInstance().getBuilding(designation.getBuilding()).construction) {
            action = new ConstructionAction(designation, itemSelectors);
        } else {
            action = new BuildingAction(designation, itemSelectors);
        }
        Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority);
        task.setDesignation(designation);
        return task;
    }

    /**
     * Removes task. called if task is finished or canceled.
     * Removes tasks designation if there is one.
     */
    public void removeTask(Task task) {
        tasks.remove(task);
        if (task.getDesignation() != null) {
            designations.remove(task.getDesignation().getPosition());
        }
    }

    /**
     * For adding simple tasks.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    public Designation getDesignation(int x, int y, int z) {
        return designations.get(cachePosition.set(x, y, z));
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public HashMap<Position, Designation> getDesignations() {
        return designations;
    }
}
