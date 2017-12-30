package stonering.objects.local_actors.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.objects.local_actors.unit.Unit;

/**
 * Created by Alexander on 10.10.2017.
 */
public abstract class Aspect {
    protected GameContainer gameContainer;
    protected String name;
    protected Unit unit;

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void init(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public void turn() {}
}
