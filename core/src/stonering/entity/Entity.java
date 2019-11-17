package stonering.entity;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Turnable;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class for all game entities. Contains its aspects.
 * TODO remove turns from all entities(move to systems).
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public abstract class Entity implements Serializable, Initable, Turnable {
    protected HashMap<Class, Aspect> aspects;
    public Position position;

    protected Entity() {
        aspects = new HashMap<>();
    }

    public Entity(Position position) {
        this();
        this.position = position != null ? position.clone() : null;
    }

    public <T extends Aspect> boolean hasAspect(Class<T> type) {
        return aspects.containsKey(type);
    }

    public <T extends Aspect> T getAspect(Class<T> type) {
        return (T) aspects.get(type);
    }

    public <T extends Aspect> void addAspect(T aspect) {
        if (aspect == null) return;
        aspect.setEntity(this);
        aspects.put(aspect.getClass(), aspect);
    }

    @Override
    public void turn() {
        aspects.values().forEach(Aspect::turn);
    }

    @Override
    public void turnUnit(TimeUnitEnum unit) {
        aspects.values().forEach(aspect -> aspect.turnUnit(unit));
    }

    @Override
    public void init() {
        aspects.values().stream().filter(aspect -> aspect instanceof Initable).forEach(aspect -> ((Initable) aspect).init());
    }

    public void setPosition(Position position) {
        if(this.position == null) this.position = new Position();
        this.position.set(position);
    }
}
