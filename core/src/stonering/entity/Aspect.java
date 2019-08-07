package stonering.entity;

import stonering.game.model.IntervalTurnable;

import java.io.Serializable;

/**
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect extends IntervalTurnable implements Serializable {
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
    public void turn() {}
}
