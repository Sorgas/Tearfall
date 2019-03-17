package stonering.entity.local;

import stonering.game.core.GameMvc;
import stonering.game.core.model.Turnable;
import stonering.util.global.Initable;

import java.io.Serializable;

/**
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect extends Turnable implements Initable, Serializable {
    protected GameMvc gameMvc;
    protected AspectHolder aspectHolder;

    public Aspect(AspectHolder aspectHolder) {
        this.aspectHolder = aspectHolder;
    }

    public abstract String getName();

    public AspectHolder getAspectHolder() {
        return aspectHolder;
    }

    public void setAspectHolder(AspectHolder aspectHolder) {
        this.aspectHolder = aspectHolder;
    }

    @Override
    public void init() {
        gameMvc = GameMvc.getInstance();
    }

    @Override
    public void turn() {}
}
