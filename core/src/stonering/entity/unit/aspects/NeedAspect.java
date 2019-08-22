package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.entity.unit.aspects.needs.Need;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.game.model.lists.units.CreatureNeedSystem;

import java.util.ArrayList;

/**
 * Holds names of creature's needs. Needs updated in a system {@link CreatureNeedSystem}.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public class NeedAspect extends Aspect {
    public ArrayList<NeedEnum> needs; // need names
    public Task satisfyingTask; // taken by planning

    public NeedAspect(Entity entity) {
        super(entity);
        needs = new ArrayList<>();
    }

    /**
     * Updates this aspect.
     */
    public void update() {
        strongestNeed = null;
        this.priority = -1;
        for (Need need : needs) {
            int priority = need.countPriority(entity);
            if (priority <= this.priority) continue; // weaker or same need
            strongestNeed = need;
            this.priority = priority;
        }
    }
}
