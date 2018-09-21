package stonering.objects.local_actors.unit.aspects.needs;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.Task;
import stonering.objects.local_actors.AspectHolder;

/**
 * Abstract class for needs.
 * Need priority should be checked before creating task for performance purposes.
 *
 * @author Alexander on 21.09.2018.
 */
public abstract class Need {
    protected AspectHolder aspectHolder;
    protected GameContainer container;
    protected float priorityMod;

    public Need() {
    }

    public void init(AspectHolder aspectHolder, GameContainer container) {
        this.aspectHolder = aspectHolder;
        this.container = container;
    }

    public abstract int countPriority();

    public abstract Task tryCreateTask();

    public float getPriorityMod() {
        return priorityMod;
    }

    public void setPriorityMod(float priorityMod) {
        this.priorityMod = priorityMod;
    }
}
