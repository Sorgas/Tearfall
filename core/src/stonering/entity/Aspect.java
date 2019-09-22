package stonering.entity;

import stonering.game.model.Turnable;

import java.io.Serializable;

/**
 * Component of an {@link Entity}
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect extends Turnable implements Serializable {
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
