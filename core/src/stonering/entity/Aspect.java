package stonering.entity;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Turnable;

import java.io.Serializable;

/**
 * Component of an {@link Entity}.
 * TODO remove turns from all aspects(move to systems).
 *
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect implements Serializable, Turnable {
    protected Entity entity;
    protected TimeUnitEnum updateUnit;

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

    @Override
    public void turnUnit(TimeUnitEnum unit) {}

}
