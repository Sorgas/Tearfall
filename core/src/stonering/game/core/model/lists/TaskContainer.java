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
import stonering.objects.jobs.actions.aspects.requirements.EquippedItemRequirementAspect;
import stonering.objects.jobs.actions.aspects.requirements.ItemsOnPositionRequirementAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;
import stonering.objects.local_actors.items.Item;
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
    public void submitDesignation(Position position, DesignationTypes type) {
        switch (type) {
            case NONE:
            case DIG:
            case STAIRS:
            case RAMP:
            case CHANNEL:
            case CHOP:
            case CUT: {
                if (validateDesignations(position, type)) {
                    OrderDesignation designation = new OrderDesignation(position, type);
                    Task task = createOrderTask(designation);
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
     */
    public void submitDesignation(Position position, String building, ArrayList<Item> items) {
        if (validateBuilding(position, building) ) {
            BuildingDesignation designation = new BuildingDesignation(position, DesignationTypes.BUILD, building);
            Task task = createBuildingTask(designation, items);
            tasks.add(task);
            designations.put(position, designation);
            container.getLocalMap().setDesignatedBlockType(designation.getPosition(), designation.getType().getCode());
            System.out.println("building designated");
        }
    }

    private Task createOrderTask(OrderDesignation designation) {
        switch (designation.getType()) {
            case DIG:
            case RAMP:
            case STAIRS:
            case CHANNEL: {
                Action action = new Action(container);
                action.setEffectAspect(new DigEffectAspect(action, designation.getType()));
                action.setTargetAspect(new BlockTargetAspect(action, designation.getPosition()));
                action.setRequirementsAspect(new EquippedItemRequirementAspect(action, "diging_tool"));
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, this, container);
                return task;
            }
            case CUT:
            case CHOP: {
                Action action = new Action(container);
                action.setEffectAspect(new ChopTreeEffectAspect(action));
                action.setTargetAspect(new BlockTargetAspect(action, designation.getPosition()));
                action.setRequirementsAspect(new EquippedItemRequirementAspect(action, "chopping_tool"));
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, this, container);
                return task;
            }
        }
        return null;
    }

    private Task createBuildingTask(BuildingDesignation designation, ArrayList<Item> items) {
        BuildingMap.getInstance().getBuilding(designation.getBuilding());
        Action action = new Action(container);
        action.setRequirementsAspect(new ItemsOnPositionRequirementAspect(action, designation.getPosition(), items));
        action.setTargetAspect(new BlockTargetAspect(action, designation.getPosition()));
        action.setEffectAspect(new ConstructionEffectAspect(action, designation.getBuilding(), "qwer"));//TODO
        return new Task("designation", TaskTypesEnum.DESIGNATION, action, this, container);
    }

    private boolean validateDesignations(Position position, DesignationTypes type) {
        BlockTypesEnum blockOnMap = BlockTypesEnum.getType(container.getLocalMap().getBlockType(position));
        switch (type) {
            case DIG: { //makes floor
                return blockOnMap.equals(BlockTypesEnum.RAMP) ||
                        blockOnMap.equals(BlockTypesEnum.WALL) ||
                        blockOnMap.equals(BlockTypesEnum.STAIRS);
            }
            case CHANNEL: { //makes space and ramp lower
                return !blockOnMap.equals(BlockTypesEnum.SPACE);
            }
            case RAMP:
            case STAIRS: {
                return blockOnMap.equals(BlockTypesEnum.WALL);
            }
            case CHOP: {
                PlantBlock block = container.getLocalMap().getPlantBlock(position);
                return block != null &&
                   (blockOnMap.equals(BlockTypesEnum.SPACE) || blockOnMap.equals(BlockTypesEnum.FLOOR))
                        && block.getPlant().getType().isTree();
            }
            case NONE: {
                return true;
            }
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
