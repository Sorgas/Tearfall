package stonering.objects.local_actors;

import stonering.game.core.model.GameContainer;

/**
 * Created by Alexander on 10.10.2017.
 */
public abstract class Aspect {
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

    public void turn() {}
}
