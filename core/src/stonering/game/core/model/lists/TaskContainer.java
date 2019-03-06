package stonering.game.core.model.lists;

import stonering.designations.BuildingDesignation;
import stonering.designations.Designation;
import stonering.designations.OrderDesignation;
import stonering.entity.jobs.actions.*;
import stonering.entity.local.building.Blueprint;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.entity.local.building.BuildingType;
import stonering.enums.designations.DesignationTypeEnum;
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
import java.util.HashMap;
import java.util.List;

/**
 * Contains all tasks for settlers on map and Designations for rendering.
 * {@link Task} are orders for units.
 * {@link Designation} are used for drawing given orders as tiles.
 * Designations
 *
 * @author Alexander Kuzyakov
 */
public class TaskContainer implements ModelComponent, Initable {
    LocalMap localMap;
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
     * Called from {@link stonering.game.core.controller.controllers.toolbar.DesignationsController}.
     * Adds designation and creates comprehensive task.
     * All single-tile buildings are constructed through this method.
     */
    public void submitBuildingDesignation(Position position, Blueprint blueprint, List<ItemSelector> itemSelectors, int priority) {
        String building = blueprint.getBuilding();
        if (!validateBuilding(position, blueprint)) return;
        BuildingDesignation designation = new BuildingDesignation(position, DesignationTypeEnum.BUILD, building);
        Task task = createBuildingTask(designation, itemSelectors, priority);
        designation.setTask(task);
        tasks.add(task);
        addDesignation(designation);
        TagLoggersEnum.TASKS.log(task.getName() + " designated");
    }

    public void submitconstructionDesignation(Position position, String construction, List<ItemSelector> itemSelectors, int priority) {
        if()
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
        }
        return null;
    }

    /**
     * Creates tasks for building various buildings.
     */
    private Task createBuildingTask(BuildingDesignation designation, List<ItemSelector> items, int priority) {
        BuildingAction buildingAction = new BuildingAction(designation, items);
        Task task = new Task("designation", TaskTypesEnum.DESIGNATION, buildingAction, priority);
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
        }
        return false;
    }

    /**
     * Validates if it's possible to build given building on given position.
     */
    private boolean validateBuilding(Position pos, String building) {
        BuildingType buildngType = BuildingTypeMap.getInstance().getBuilding(building);
        String category = buildngType.getCategory();
        boolean result = false;
        byte blockType = localMap.getBlockType(pos);
        switch (category) {
            case "constructions": {
                result = blockType == BlockTypesEnum.SPACE.CODE || blockType == BlockTypesEnum.FLOOR.CODE;
                break;
            }
            case "workbenches": {
                result = blockType == BlockTypesEnum.FLOOR.CODE;
                break;
            }
        }
        TagLoggersEnum.TASKS.logDebug(building + " validation " + (result ? "passed." : "failed."));
        return result;
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
}
