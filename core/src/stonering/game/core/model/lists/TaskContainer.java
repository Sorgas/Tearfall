package stonering.game.core.model.lists;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.ActionTypeEnum;
import stonering.objects.jobs.actions.TaskTypesEnum;
import stonering.objects.jobs.actions.aspects.effect.DigEffectAspect;
import stonering.objects.jobs.actions.aspects.requirements.EquippedItemRequirementAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;

import java.util.ArrayList;

/**
 * Created by Alexander on 14.06.2017.
 *
 * contains all tasks for settlers on map
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
            if (task.isTaskTargetsAvaialbleFrom(pos)) {
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

    public void addDesignation(Position position, DesignationTypes blockType) {
        if (validateDesignations(position, blockType)) {
            Designation designation = new Designation(position, blockType);
            int index = designations.indexOf(designation);
            if (index >= 0) {
                tasks.remove(designation.task);
                designations.add(index, designation);
            } else {
                designations.add(designation);
            }
            designation.task = createDesignationTask(designation);
            tasks.add(designation.task);
            container.getLocalMap().setDesignatedBlockType(designation.position, designation.type.getCode());
        }
    }

    private Task createDesignationTask(Designation designation) {
        Action action = new Action(ActionTypeEnum.DIG, container);
        action.setEffectAspect(new DigEffectAspect(action, designation.type));
        action.setTargetAspect(new BlockTargetAspect(action, designation.position));
        action.setRequirementsAspect(new EquippedItemRequirementAspect(action, "tool", "dig"));
        Task task = new Task("designation", TaskTypesEnum.DESIGNATION, action, this, container);
        return task;
    }

    private boolean validateDesignations(Position position, DesignationTypes blockType) {
        BlockTypesEnum blockOnMap = BlockTypesEnum.getType(container.getLocalMap().getBlockType(position));
        switch (blockType) {
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
