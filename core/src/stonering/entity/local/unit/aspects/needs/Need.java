package stonering.entity.local.unit.aspects.needs;

import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.entity.job.Task;
import stonering.entity.local.AspectHolder;
import stonering.util.global.Initable;

/**
 * Abstract class for needs.
 * Need priority should be checked before creating task for performance purposes.
 * TODO refactor to system.
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public abstract class Need implements Initable {
    protected GameModel container;
    protected float priorityMod;

    public Need() {
    }

    public void init() {
        this.container = GameMvc.instance().getModel();
    }

    /**
     * Returns priority of need. Returns -1 if need can be tolerated.
     * @param aspectHolder
     */
    public abstract int countPriority(AspectHolder aspectHolder);

    public abstract Task tryCreateTask(AspectHolder aspectHolder);

    public float getPriorityMod() {
        return priorityMod;
    }

    public void setPriorityMod(float priorityMod) {
        this.priorityMod = priorityMod;
    }
}
