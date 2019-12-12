package stonering.entity;

import stonering.game.model.Updatable;

import java.io.Serializable;

/**
 * Component of an {@link Entity}.
 * TODO remove turns from all aspects(move to systems).
 *
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect implements Serializable, Updatable {
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
}
