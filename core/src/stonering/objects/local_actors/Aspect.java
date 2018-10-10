package stonering.objects.local_actors;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.Turnable;

/**
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect extends Turnable {
    protected GameContainer gameContainer;
    protected String name;
    protected AspectHolder aspectHolder;

    public Aspect(String name, AspectHolder aspectHolder) {
        this.name = name;
        this.aspectHolder = aspectHolder;
    }

    public String getName() {
        return name;
    }

    public AspectHolder getAspectHolder() {
        return aspectHolder;
    }

    public void setAspectHolder(AspectHolder aspectHolder) {
        this.aspectHolder = aspectHolder;
    }

    public void init(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    @Override
    public void turn() {}
}
