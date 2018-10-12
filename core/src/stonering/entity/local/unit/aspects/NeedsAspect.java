package stonering.entity.local.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.entity.jobs.Task;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.unit.aspects.needs.Need;

import java.util.ArrayList;

/**
 * Generates tasks for satisfying creature needs.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public class NeedsAspect extends Aspect {
    private ArrayList<Need> needs;
    private int needsCheckDelay;
    private Need strongestNeed;
    private int priority;

    public NeedsAspect(AspectHolder aspectHolder) {
        super("needs", aspectHolder);
        needs = new ArrayList<>();
    }

    @Override
    public void init(GameContainer gameContainer) {
        super.init(gameContainer);
        needs.forEach(need -> need.init(aspectHolder, gameContainer));
    }

    public void initNeeds() {
    }

    public Task getNeed() {
        return null;
    }


    /**
     * Updates current need and its priority.
     */
    public void update() {
        strongestNeed = null;
        this.priority = -1;
        for (Need need : needs) {
            int priority = need.countPriority();
            if (priority > this.priority) {
                strongestNeed = need;
                this.priority = priority;
            }
        }
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
