package stonering.objects.aspects;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.actions.Action;

public class ActionAspect extends Aspect {

    @Override
    public void init(GameContainer gameContainer) {

    }

    public void performAction(Action action) {
        System.out.println("action performed");
    }
}
