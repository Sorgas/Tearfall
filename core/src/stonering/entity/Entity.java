package stonering.entity;

import stonering.game.model.Turnable;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class for all game entities. Contains its aspects.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public abstract class Entity extends Turnable implements Serializable, Initable {
    protected HashMap<Class, Aspect> aspects;
    public Position position;

    protected Entity() {
        aspects = new HashMap<>();
    }

    public Entity(Position position) {
        this();
        this.position = position;
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

    public void turn() {
        aspects.values().forEach(Aspect::turn);
    }

    @Override
    public void init() {
        for (Aspect aspect : aspects.values()) {
            if(aspect instanceof Initable) ((Initable) aspect).init();
        }
    }

    public void setPosition(Position position) {
        if(this.position == null) this.position = new Position();
        this.position.set(position);
    }
}
