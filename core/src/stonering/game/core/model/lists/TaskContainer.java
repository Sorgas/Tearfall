package stonering.game.core.model.lists;

import stonering.designations.BuildingDesignation;
import stonering.designations.Designation;
import stonering.designations.OrderDesignation;
import stonering.entity.jobs.actions.*;
import stonering.entity.local.building.BuildingOrder;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.model.ModelComponent;
import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.jobs.Task;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.plants.PlantBlock;
import stonering.util.global.Initable;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Contains all tasks for settlers on map and Designations for rendering.
 * {@link Task} are orders for units.
 * {@link Designation} are used for drawing given orders as tiles.
 * Designations
 *
 * @author Alexander Kuzyakov
 */
public class TaskContainer implements ModelComponent, Initable {
    private LocalMap localMap;
    private ArrayList<Task> tasks;
    private HashMap<Position, Designation> designations;

    public TaskContainer() {
        tasks = new ArrayList<>();
        designations = new HashMap<>();
    }

    @Override
    public void init() {
        localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
    }

    public Task getActiveTask(Position pos) {
        for (Task task : tasks) {
            if (task.getPerformer() == null && task.isTaskTargetsAvaialbleFrom(pos)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Called from {@link stonering.game.core.controller.controllers.toolbar.DesignationsController}.
     * Adds designation and creates comprehensive task.
     * All simple orders like digging and foraging submitted through this method.
     *
     * @param position
     * @param type
     */
    public void submitOrderDesignation(Position position, DesignationTypeEnum type, int priority) {
        switch (type) {
            case BUILD:
            case NONE:
            case DIG:
            case STAIRS:
            case RAMP:
            case CHANNEL:
            case CHOP:
            case HARVEST:
            case CUT: {
                if (validateDesignations(position, type)) {
                    OrderDesignation designation = new OrderDesignation(position, type);
                    Task task = createOrderTask(designation, priority);
                    if (task != null) {
                        task.setDesignation(designation);
                        designation.setTask(task);
                        tasks.add(task);
                        addDesignation(designation);
                    }
                }
            }
        }
    }

    /**
     * Called from {@link stonering.game.core.controller.controllers.designation.BuildingDesignationSequence}.
     * Adds designation and creates comprehensive task.
     * All single-tile buildings are constructed through this method.
     */
    public void submitBuildingDesignation(BuildingOrder order, int priority) {
        Position position = order.getPosition();
        if (!PlaceValidatorsEnum.getValidator(order.getBlueprint().getPlacing()).validate(localMap, position)) return;
        BuildingDesignation designation = new BuildingDesignation(position, DesignationTypeEnum.BUILD, order.getBlueprint().getBuilding());
        Task task = createBuildingTask(designation, order.getItemSelectors().values(), priority);
        designation.setTask(task);
        tasks.add(task);
        addDesignation(designation);
        TagLoggersEnum.TASKS.log(task.getName() + " designated");
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
            case CUT:
            case CHOP: {
                ChopTreeAction chopTreeAction = new ChopTreeAction(designation);
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, chopTreeAction, priority);
                return task;
            }
            case HARVEST: {
                PlantBlock block = localMap.getPlantBlock(designation.getPosition());
                if (block != null && block.getPlant().isHarvestable()) {
                    PlantHarvestAction plantHarvestAction = new PlantHarvestAction(block.getPlant());
                    Task task = new Task("designation", TaskTypesEnum.DESIGNATION, plantHarvestAction, priority);
                    return task;
                }
            }
            case FARM: {

            }
        }
        return null;
    }

    /**
     * Creates tasks for building various buildings.
     */
    private Task createBuildingTask(BuildingDesignation designation, Collection<ItemSelector> itemSelectors, int priority) {
        Action action;
        if(BuildingTypeMap.getInstance().getBuilding(designation.getBuilding()).isConstruction()) {
            action = new ConstructionAction(designation, itemSelectors);
        } else {
            action = new BuildingAction(designation, itemSelectors);
        }
        Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority);
        task.setDesignation(designation);
        return task;
    }

    private boolean validateDesignations(Position position, DesignationTypeEnum type) {
        BlockTypesEnum blockOnMap = BlockTypesEnum.getType(localMap.getBlockType(position));
        switch (type) {
            case DIG: { //makes floor
                return BlockTypesEnum.RAMP.equals(blockOnMap) ||
                        BlockTypesEnum.WALL.equals(blockOnMap) ||
                        BlockTypesEnum.STAIRS.equals(blockOnMap);
            }
            case CHANNEL: { //makes space and ramp lower
                return !BlockTypesEnum.SPACE.equals(blockOnMap);
            }
            case RAMP:
            case STAIRS: {
                return BlockTypesEnum.WALL.equals(blockOnMap);
            }
            case CHOP: {
                //TODO designate tree as whole
                PlantBlock block = localMap.getPlantBlock(position);
                return block != null &&
                        (BlockTypesEnum.SPACE.equals(blockOnMap) || BlockTypesEnum.FLOOR.equals(blockOnMap))
                        && block.getPlant().getType().isTree();
            }
            case NONE: {
                return true;
            }
            case CUT:
                break;
            case HARVEST:
                //TODO add harvesting from trees
                PlantBlock block = localMap.getPlantBlock(position);
                return block != null && !block.getPlant().getType().isTree();
            case BUILD:
                break;
            case FARM:
                return ZoneTypesEnum.FARM.getValidator().validate(localMap, position);
        }
        return false;
    }

    /**
     * Removes task. called if task is finished or canceled.
     * Removes tasks designation if there is one.
     *
     * @param task
     */
    public void removeTask(Task task) {
        tasks.remove(task);
        if (task.getDesignation() != null) {
            removeDesignation(task.getDesignation());
        }
    }

    /**
     * Adds designation to designations map. Updates local map.
     *
     * @param designation
     */
    private void addDesignation(Designation designation) {
        designations.put(designation.getPosition(), designation);
        localMap.setDesignatedBlockType(designation.getPosition(), designation.getType().CODE);
    }

    /**
     * Removes designation from designations map. Updates local map.
     *
     * @param designation
     */
    private void removeDesignation(Designation designation) {
        designations.remove(designation.getPosition());
        localMap.setDesignatedBlockType(designation.getPosition(), DesignationTypeEnum.NONE.CODE);
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
