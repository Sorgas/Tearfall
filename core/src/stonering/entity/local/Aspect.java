package stonering.entity.local;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.Turnable;

import java.io.Serializable;

/**
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect extends Turnable implements Serializable {
    protected GameContainer gameContainer;
    public static String NAME;
    protected AspectHolder aspectHolder;

    public Aspect( AspectHolder aspectHolder) {
        this.aspectHolder = aspectHolder;
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
