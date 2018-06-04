package stonering.game.core.model.lists;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.ActionTypeEnum;
import stonering.objects.jobs.actions.TaskTypesEnum;
import stonering.objects.jobs.actions.aspects.effect.ChopTreeEffectAspect;
import stonering.objects.jobs.actions.aspects.effect.DigEffectAspect;
import stonering.objects.jobs.actions.aspects.requirements.EquippedItemRequirementAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;
import stonering.objects.local_actors.plants.PlantBlock;

import java.util.ArrayList;

/**
 * Contains all tasks for settlers on map. Designations stored separately for updating tiles.
 * <p>
 * Created by Alexander on 14.06.2017.
 */
public class TaskContainer {
    private GameContainer container;
    private ArrayList<Task> tasks;
    private ArrayList<Designation> designations;

    public TaskContainer(GameContainer container) {
        this.container = container;
        tasks = new ArrayList<>();
        designations = new ArrayList<>();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Task getActiveTask(Position pos) {
        for (Task task : tasks) {
            if (task.getPerformer() == null && task.isTaskTargetsAvaialbleFrom(pos)) {
                return task;
            }
        }
        return null;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addDesignation(Position position, DesignationTypes type) {
        if (validateDesignations(position, type)) {
            Designation designation = new Designation(position, type);
            designation.task = createDesignationTask(designation);
            if (designation.task != null) {
                int index = designations.indexOf(designation);
                if (index >= 0) {
                    tasks.remove(designation.task);
                    designations.add(index, designation);
                } else {
                    designations.add(designation);
                }
                tasks.add(designation.task);
                container.getLocalMap().setDesignatedBlockType(designation.position, designation.type.getCode());
            }
        }
    }

    private Task createDesignationTask(Designation designation) {
        switch (designation.type) {
            case DIG:
            case RAMP:
            case STAIRS:
            case CHANNEL: {
                Action action = new Action(container);
                action.setEffectAspect(new DigEffectAspect(action, designation.type));
                action.setTargetAspect(new BlockTargetAspect(action, designation.position));
                action.setRequirementsAspect(new EquippedItemRequirementAspect(action, "diging_tool"));
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, this, container);
                return task;
            }
            case CUT:
            case CHOP: {
                Action action = new Action(container);
                action.setEffectAspect(new ChopTreeEffectAspect(action));
                action.setTargetAspect(new BlockTargetAspect(action, designation.position));
                action.setRequirementsAspect(new EquippedItemRequirementAspect(action, "chopping_tool"));
                Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, this, container);
                return task;
            }
        }
        return null;
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

    public void removeTask(Task task) {
        tasks.remove(task);
        if (task.getTaskType() == TaskTypesEnum.DESIGNATION) {
            container.getLocalMap().setDesignatedBlockType(task.getInitialAction().getTargetPosition(), DesignationTypes.NONE.getCode());
        }
    }

    private class Designation {
        Position position;
        DesignationTypes type;
        Task task;

        public Designation(Position position, DesignationTypes blockType) {
            this.position = position;
            this.type = blockType;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj != null && obj.getClass().equals(Designation.class)) && position.equals(((Designation) obj).position);
        }
    }
}
