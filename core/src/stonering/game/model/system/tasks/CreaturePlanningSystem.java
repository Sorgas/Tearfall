package stonering.game.model.system.tasks;

import stonering.entity.job.ItemOrderTask;
import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.items.ItemContainer;
import stonering.util.global.Logger;

/**
 * System for giving {@link Task}s to {@link Unit}s.
 * Tasks are stored in units {@link PlanningAspect}.
 *
 * @author Alexander on 28.10.2019.
 */
public class CreaturePlanningSystem {

    public void assignTaskToUnit(Task task, Unit unit) {
        if(!GameMvc.instance().getModel().get(TaskContainer.class).claimTask(task)) return; // lock task in container
        unit.getAspect(PlanningAspect.class).updateState(task); // give task to unit
    }

    /**
     * Finished tasks are removed completely.
     */
    public void finishTask(Task task) {
        removeTask(task);
        if(task instanceof ItemOrderTask) setItemOrderStatus((ItemOrderTask) task, TaskStatusEnum.COMPLETE);
    }

    /**
     * Failed tasks
     */
    public void failTask(Task task) {
        removeTask(task);
        if(task instanceof ItemOrderTask) setItemOrderStatus((ItemOrderTask) task, TaskStatusEnum.FAILED);
    }

    /**
     * Completely removes task from game. Used for finished and failed tasks. Repeated tasks in workbenches are handled separately.
     * Task should be assigned in {@link TaskContainer} ans have performer.
     */
    public void removeTask(Task task) {
        if(task.performer != null) {
            TaskContainer container = GameMvc.instance().getModel().get(TaskContainer.class);
            if(!container.assignedTasks.remove(task)) Logger.TASKS.logError("Finished task was not assigned."); // remove from container
            GameMvc.instance().getModel().get(ItemContainer.class).freeItems(task.lockedItems); // free items
            task.performer.getAspect(PlanningAspect.class).updateState(null); // free unit
        } else {
            // TODO remove not assigned tasks.
        }
    }

    private void setItemOrderStatus(ItemOrderTask task, TaskStatusEnum status) {
        task.order.status = status;
    }
}
