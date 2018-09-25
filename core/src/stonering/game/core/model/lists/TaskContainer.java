package stonering.game.core.model.lists;

import stonering.designations.BuildingDesignation;
import stonering.designations.Designation;
import stonering.designations.OrderDesignation;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingMap;
import stonering.enums.buildings.BuildingType;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.TaskTypesEnum;
import stonering.objects.jobs.actions.aspects.effect.ConstructionEffectAspect;
import stonering.objects.jobs.actions.aspects.effect.ChopTreeEffectAspect;
import stonering.objects.jobs.actions.aspects.effect.DigEffectAspect;
import stonering.objects.jobs.actions.aspects.effect.HarvestPlantEffectAspect;
import stonering.objects.jobs.actions.aspects.requirements.EquippedItemRequirementAspect;
import stonering.objects.jobs.actions.aspects.requirements.ItemsInPositionOrInventoryRequirementAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;
import stonering.objects.jobs.actions.aspects.target.PlantHarvestTargetAspect;
import stonering.objects.jobs.actions.aspects.target.PlantTargetAspect;
import stonering.objects.local_actors.items.selectors.ItemSelector;
import stonering.objects.local_actors.items.selectors.ToolWithActionItemSelector;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains all tasks for settlers on map. Designations stored separately for updating tiles.
 * <p>
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
     * Adds designation and creates comprehensive task.
     * All simple orders like digging and foraging submitted through this method.
     *
     * @param position
     * @param type
     */
    public void submitDesignation(Position position, DesignationTypes type, int priority) {
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
                        designations.put(position, designation);
                        tasks.add(task);
                        container.getLocalMap().setDesignatedBlockType(designation.getPosition(), designation.getType().getCode());
                    }
                }
            }
        }
    }

    /**
     * Adds designation and creates comprehensive task.
     * All single-tile buildings are constructed through this method.
     *
     * @param position
     * @param building
     * @param itemSelectors
     */
    public void submitDesignation(Position position, String building, ArrayList<ItemSelector> itemSelectors, int priority) {
        if (validateBuilding(position, building)) {
            BuildingDesignation designation = new BuildingDesignation(position, DesignationTypes.BUILD, building);
            Task task = createBuildingTask(designation, itemSelectors, priority);
            tasks.add(task);
            designation.setTask(task);
            designations.put(position, designation);
            container.getLocalMap().setDesignatedBlockType(designation.getPosition(), designation.getType().getCode());
            System.out.println("building designated");
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
                Action action = new Action(container);
                action.setEffectAspect(new DigEffectAspect(action, designation.getType()));
                action.setTargetAspect(new BlockTargetAspect(action, designation.getPosition(), true, true));
                action.setRequirementsAspect(new EquippedItemRequirementAspect(action, new ToolWithActionItemSelector("dig")));
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority, container);
                return task;
            }
            case CUT:
            case CHOP: {
                Action action = new Action(container);
                action.setEffectAspect(new ChopTreeEffectAspect(action));
                action.setTargetAspect(new BlockTargetAspect(action, designation.getPosition(), false, true)); //TODO replace with PlantTargetAspect
                action.setRequirementsAspect(new EquippedItemRequirementAspect(action, new ToolWithActionItemSelector("chop")));
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority, container);
                return task;
            }
            case HARVEST: {
                PlantBlock block = container.getLocalMap().getPlantBlock(designation.getPosition());
                if(block != null && block.getPlant().isHarvestable()) {
                    Action action = new Action(container);
                    action.setEffectAspect(new HarvestPlantEffectAspect(action, 10));
                    action.setTargetAspect(new PlantHarvestTargetAspect(action, block.getPlant())); //TODO replace with PlantTargetAspect
                    action.setRequirementsAspect(new EquippedItemRequirementAspect(action, new ToolWithActionItemSelector("harvest_plants")));
                    Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, priority, container);
                    return task;
                }
            }

        }
        return null;
    }

    private Task createBuildingTask(BuildingDesignation designation, ArrayList<ItemSelector> items, int priority) {
        BuildingType buildingType = BuildingMap.getInstance().getBuilding(designation.getBuilding());
        Action action = new Action(container);
        action.setRequirementsAspect(new ItemsInPositionOrInventoryRequirementAspect(action, designation.getPosition(), items));
        action.setTargetAspect(new BlockTargetAspect(action, designation.getPosition(), !buildingType.getTitle().equals("wall"), true));
        action.setEffectAspect(new ConstructionEffectAspect(action, designation.getBuilding(), "marble"));//TODO
        return new Task("designation", TaskTypesEnum.DESIGNATION, action, priority, container);
    }

    private boolean validateDesignations(Position position, DesignationTypes type) {
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


    public boolean validateBuilding(Position pos, String building) {
        BuildingType buildngType = BuildingMap.getInstance().getBuilding(building);
        String category = buildngType.getCategory();
        switch (category) {
            case "constructions": {
                byte blockType = container.getLocalMap().getBlockType(pos);
                return blockType == BlockTypesEnum.SPACE.getCode() || blockType == BlockTypesEnum.FLOOR.getCode();
            }
        }
        return false;
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        if (task.getDesignation() != null) {
            designations.remove(task.getDesignation().getPosition());
        }
        if (task.getTaskType() == TaskTypesEnum.DESIGNATION) {
            container.getLocalMap().setDesignatedBlockType(task.getInitialAction().getTargetPosition(), DesignationTypes.NONE.getCode());
        }
    }

    public void createTask() {

    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
