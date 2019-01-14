package stonering.game.core.model.lists;

import stonering.designations.BuildingDesignation;
import stonering.designations.Designation;
import stonering.designations.OrderDesignation;
import stonering.entity.jobs.actions.aspects.effect.*;
import stonering.entity.jobs.actions.aspects.target.PositionActionTarget;
import stonering.entity.jobs.actions.aspects.target.PlantHarvestActionTarget;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.entity.local.building.BuildingType;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.model.GameContainer;
import stonering.util.geometry.Position;
import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.TaskTypesEnum;
import stonering.entity.jobs.actions.aspects.requirements.EquippedItemRequirementAspect;
import stonering.entity.jobs.actions.aspects.requirements.ItemsInPositionOrInventoryRequirementAspect;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.ToolWithActionItemSelector;
import stonering.entity.local.plants.PlantBlock;
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
public class TaskContainer {
    private GameContainer container;
    private ArrayList<Task> tasks;
    private HashMap<Position, Designation> designations;

    public TaskContainer(GameContainer container) {
        this.container = container;
        tasks = new ArrayList<>();
        designations = new HashMap<>();
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
     *
     * @param position
     * @param building
     * @param itemSelectors
     */
    public void submitBuildingDesignation(Position position, String building, List<ItemSelector> itemSelectors, int priority) {
        if (validateBuilding(position, building)) {
            BuildingDesignation designation = new BuildingDesignation(position, DesignationTypeEnum.BUILD, building);
            Task task = createBuildingTask(designation, itemSelectors, priority);
            designation.setTask(task);
            tasks.add(task);
            addDesignation(designation);
            TagLoggersEnum.TASKS.log(task.getName() + " designated");
        }
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
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, digAction, priority, container);
                return task;
            }
            case CUT:
            case CHOP: {
                Action action = new Action(container);
                action.setEffectAspect(new ChopTreeEffectAspect(action));
                action.setTargetAspect(new PositionActionTarget(action, designation.getPosition(), false, true)); //TODO replace with PlantActionTarget
                action.setRequirementsAspect(new EquippedItemRequirementAspect(action, new ToolWithActionItemSelector("chop")));
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority, container);
                return task;
            }
            case HARVEST: {
                PlantBlock block = container.getLocalMap().getPlantBlock(designation.getPosition());
                if (block != null && block.getPlant().isHarvestable()) {
                    Action action = new Action(container);
                    action.setEffectAspect(new HarvestPlantEffectAspect(action, 10));
                    action.setTargetAspect(new PlantHarvestActionTarget(action, block.getPlant())); //TODO replace with PlantActionTarget
                    action.setRequirementsAspect(new EquippedItemRequirementAspect(action, new ToolWithActionItemSelector("harvest_plants")));
                    Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority, container);
                    return task;
                }
            }

        }
        return null;
    }

    /**
     * Creates tasks for building various buildings.
     *
     * @param designation
     * @param items
     * @param priority
     * @return
     */
    private Task createBuildingTask(BuildingDesignation designation, List<ItemSelector> items, int priority) {
        BuildingType buildingType = BuildingTypeMap.getInstance().getBuilding(designation.getBuilding());
        Action action = new Action(container);
        action.setRequirementsAspect(new ItemsInPositionOrInventoryRequirementAspect(action, designation.getPosition(), items));
        action.setTargetAspect(new PositionActionTarget(action, designation.getPosition(), !buildingType.getTitle().equals("wall"), true));
        action.setEffectAspect(new ConstructionEffectAspect(action, designation.getBuilding(), "marble"));
        Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority, container);
        task.setDesignation(designation);
        return task;
    }

    private boolean validateDesignations(Position position, DesignationTypeEnum type) {
        BlockTypesEnum blockOnMap = BlockTypesEnum.getType(container.getLocalMap().getBlockType(position));
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
                PlantBlock block = container.getLocalMap().getPlantBlock(position);
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
                PlantBlock block = container.getLocalMap().getPlantBlock(position);
                return block != null && !block.getPlant().getType().isTree();
            case BUILD:
                break;
        }
        return false;
    }

    /**
     * Validates if it's possible to build given building on given position.
     *
     * @param pos
     * @param building
     * @return
     */
    public boolean validateBuilding(Position pos, String building) {
        BuildingType buildngType = BuildingTypeMap.getInstance().getBuilding(building);
        String category = buildngType.getCategory();
        boolean result = false;
        byte blockType = container.getLocalMap().getBlockType(pos);
        switch (category) {
            case "constructions": {
                result = blockType == BlockTypesEnum.SPACE.getCode() || blockType == BlockTypesEnum.FLOOR.getCode();
                break;
            }
            case "workbenches": {
                result = blockType == BlockTypesEnum.FLOOR.getCode();
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
     * @param designation
     */
    private void addDesignation(Designation designation) {
        designations.put(designation.getPosition(), designation);
        container.getLocalMap().setDesignatedBlockType(designation.getPosition(), designation.getType().getCode());
    }

    /**
     * Removes designation from designations map. Updates local map.
     * @param designation
     */
    private void removeDesignation(Designation designation) {
        designations.remove(designation.getPosition());
        container.getLocalMap().setDesignatedBlockType(designation.getPosition(), DesignationTypeEnum.NONE.getCode());
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
