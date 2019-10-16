package stonering.entity.job;

import stonering.entity.job.designation.Designation;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.unit.Unit;
import stonering.util.global.Logger;

import java.util.LinkedList;

import static stonering.enums.unit.job.JobsEnum.NONE;

/**
 * Task object for unit behavior in the game.
 * Consists of main action, sequence of actions to be performed before main, and after main.
 * Firstly pre action with lowest indexes are executed, then initial action, and then post action with lowest indexes.
 *
 * @author Alexander Kuzyakov
 */
public class Task {
    private String name;
    private Unit performer;
    private TaskTypesEnum taskType;
    public Designation designation;
    private ItemOrder itemOrder;
    private int priority;
    public TaskStatusEnum status;
    private String job;

    private Action initialAction;
    private LinkedList<Action> preActions;
    private LinkedList<Action> postActions;

    public Task(String name, TaskTypesEnum taskType, Action initialAction, int priority) {
        this.name = name;
        this.taskType = taskType;
        this.initialAction = initialAction;
        initialAction.setTask(this);
        preActions = new LinkedList<>();
        postActions = new LinkedList<>();
        this.priority = priority;
        status = TaskStatusEnum.OPEN;
        job = NONE.NAME;
    }

    /**
     * Removes this task from container  if it's finished.
     */
    public void tryFinishTask() {
        if (isFinished()) GameMvc.instance().getModel().get(TaskContainer.class).removeTask(this);
    }

    /**
     * Returns next action to be performed.
     */
    public Action getNextAction() {
        if (!preActions.isEmpty()) return preActions.get(0);
        if (!initialAction.isFinished()) return initialAction;
        if (!postActions.isEmpty()) return postActions.get(0);
        return null;
    }

    /**
     * Resets this task if it was failed to return it to container.
     */
    public void reset() {
        Logger.TASKS.logDebug("Resetting task " + toString());
        preActions.clear();
        postActions.clear();
        if (performer == null) return;
        PlanningAspect planningAspect = (performer.getAspect(PlanningAspect.class));
        performer = null;
        planningAspect.interrupt();
    }

    /**
     * Task is finished, if initial action is finished, and no other action remain.
     */
    public boolean isFinished() {
        return preActions.isEmpty() && initialAction.isFinished() && postActions.isEmpty();
    }

    /**
     * Removes pre and post action from task
     */
    public void finishAction(Action action) {
        if (action != initialAction) {
            preActions.remove(action);
            postActions.remove(action);
        }
    }

    public void fail() {
        //TODO add interruption
        reset();
        GameMvc.instance().getModel().get(TaskContainer.class).removeTask(this);
    }

    public boolean isTaskTargetsAvailableFrom(Position position) {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        UtilByteArray area = localMap.getPassage().getArea();
        int sourceArea = area.getValue(position);
        Position target = initialAction.getActionTarget().getPosition();
        for (int x = target.x - 1; x <= target.x + 1; x++) {
            for (int y = target.y - 1; y <= target.y + 1; y++) {
                if (localMap.inMap(x, y, target.z) &&
                        (x != target.x || y != target.y) &&
                        area.getValue(x, y, target.z) == sourceArea) return true;
            }
        }
        return false;
    }

    /**
     * This action will be executed in the first place
     */
    public void addFirstPreAction(Action action) {
        preActions.add(0, action);
        Logger.TASKS.logDebug("Action " + action + " added to task " + name);
        action.setTask(this);
    }

    /**
     * This action will be executed just before main action.
     */
    public void addLastPreAction(Action action) {
        preActions.add(action);
        action.setTask(this);
    }

    /**
     * This action will be executed right after main action.
     */
    public void addFirstPostAction(Action action) {
        preActions.add(0, action);
        action.setTask(this);
    }

    /**
     * This action will be executed in the last place.
     */
    public void addLastPostAction(Action action) {
        preActions.add(action);
        action.setTask(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskTypesEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypesEnum taskType) {
        this.taskType = taskType;
    }

    public Unit getPerformer() {
        return performer;
    }

    public Action getInitialAction() {
        return initialAction;
    }

    public void setInitialAction(Action initialAction) {
        this.initialAction = initialAction;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setPerformer(Unit performer) {
        this.performer = performer;
    }

    public ItemOrder getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(ItemOrder itemOrder) {
        this.itemOrder = itemOrder;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return name;
    }
}