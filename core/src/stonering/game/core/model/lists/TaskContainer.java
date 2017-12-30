package stonering.game.core.model.lists;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTypes;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.ActionTypeEnum;
import stonering.objects.jobs.actions.TaskTypesEnum;
import stonering.objects.jobs.actions.aspects.effect.DigEffectAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;

import java.util.ArrayList;

/**
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

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addDesignation(Position position, DesignationsTypes blockType) {
        if (validateDesignations(position, blockType)) {
            Designation designation = new Designation(position, blockType);
            int index = designations.indexOf(designation);
            if (index >= 0) {
                tasks.remove(designation.task);
                designations.add(index, designation);
            } else {
                designations.add(designation);
            }
            tasks.add(createDesignationTask(designation));
            container.getLocalMap().setDesignatedBlockType(designation.position, designation.type.getCode());
        }
    }

    private Task createDesignationTask(Designation designation) {
        Task task = new Task("designation", TaskTypesEnum.DESIGNATION);
        Action action = new Action();
        action.setActionType(ActionTypeEnum.DIG);
        action.setTask(task);
        action.setEffectAspect(new DigEffectAspect(designation.type));
        action.setTargetAspect(new BlockTargetAspect(designation.position));
//        action.setRequirementsAspect();
        task.addAction(action);
        return task;
    }

    private boolean validateDesignations(Position position, DesignationsTypes blockType) {
        switch (blockType) {
            case DIG: {
                if (BlockTypesEnum.getType(container.getLocalMap().getBlockType(position)).equals(BlockTypesEnum.RAMP) ||
                        BlockTypesEnum.getType(container.getLocalMap().getBlockType(position)).equals(BlockTypesEnum.WALL) ||
                        BlockTypesEnum.getType(container.getLocalMap().getBlockType(position)).equals(BlockTypesEnum.STAIRS)) {
                    return true;
                }
            }
        }
        return false;
    }

    private class Designation {
        Position position;
        DesignationsTypes type;
        Task task;

        public Designation(Position position, DesignationsTypes blockType) {
            this.position = position;
            this.type = blockType;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj != null && obj.getClass().equals(Designation.class)) && position.equals(((Designation) obj).position);
        }
    }
}
