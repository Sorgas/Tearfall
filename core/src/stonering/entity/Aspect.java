package stonering.entity;

import stonering.game.GameMvc;
import stonering.game.model.IntervalTurnable;
import stonering.util.global.Initable;

import java.io.Serializable;

/**
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect extends IntervalTurnable implements Initable, Serializable {
    protected GameMvc gameMvc;
    protected Entity entity;

    public Aspect(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void init() {
        gameMvc = GameMvc.instance();
    }

    @Override
    public void turn() {}
}
