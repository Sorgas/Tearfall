package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.needs.Need;

import java.util.ArrayList;

/**
 * Generates tasks for satisfying creature needs.
 * Stores list of {@link Need} and checks them periodically.
 * Needs that fail checks generate tasks for satisfying
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public class NeedsAspect extends Aspect {
    public static final String NAME = "needs";
    public static final int maxNeedsCheckDelay = 10;
    private ArrayList<Need> needs;
    private int needsCheckDelay;

    private Need strongestNeed;
    private int priority;

    public NeedsAspect(Entity entity) {
        super(entity);
        needs = new ArrayList<>();
        needsCheckDelay = maxNeedsCheckDelay;
    }

    /**
     * Once in maxNeedsCheckDelay turns updates current need and its priority.
     * Strongest need for tasks then taken by {@link PlanningAspect}
     */
    @Override
    public void turn() {
        if(needsCheckDelay-- > 0) return;
        needsCheckDelay = maxNeedsCheckDelay;
        update();
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

    @Override
    public void init() {
        needs.forEach(Need::init);
    }

    public Need getStrongestNeed() {
        return strongestNeed;
    }

    public int getPriority() {
        return priority;
    }

    public ArrayList<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(ArrayList<Need> needs) {
        this.needs = needs;
    }
}
