package stonering.objects.local_actors.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.Task;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.unit.aspects.needs.Need;

import java.util.ArrayList;

/**
 * Generates tasks for satisfying creature needs.
 *
 * @author Alexander on 16.09.2018.
 */
public class NeedsAspect extends Aspect {
    private ArrayList<Need> needs;
    private int needsCheckDelay;

    public NeedsAspect(AspectHolder aspectHolder) {
        super("needs", aspectHolder);
        needs = new ArrayList<>();
    }

    @Override
    public void init(GameContainer gameContainer) {
        super.init(gameContainer);
        needs.forEach(need -> need.init(aspectHolder, gameContainer));
    }

    @Override
    public void turn() {
        needsCheckDelay--;
        if (needsCheckDelay <= 0) {
            Need need = selectNeed();
            if(need != null) {

            }
            needsCheckDelay = 50;
        }
    }

    public void initNeeds() {
    }

    public Task getNeed() {
        return null;
    }

    private Need selectNeed() {
        Need strongestNeed = null;
        int highestPriority = -1;
        for (Need need : needs) {
            int priority = need.countPriority();
            if (priority > highestPriority) {
                strongestNeed = need;
                highestPriority = priority;
            }
        }
        return strongestNeed;
    }

    public ArrayList<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(ArrayList<Need> needs) {
        this.needs = needs;
    }
}
