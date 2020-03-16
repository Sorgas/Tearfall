package stonering.entity;

import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

/**
 * Class for all game entities. Contains its aspects.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public abstract class Entity implements Serializable, Initable, Cloneable {
    public final HashMap<Class, Aspect> aspects;
    public Position position;

    protected Entity() {
        aspects = new HashMap<>();
    }

    public Entity(Position position) {
        this();
        this.position = position != null ? position.clone() : null;
    }

    public Entity(Entity entity) {
        this.position = entity.position;
        this.aspects = new HashMap<>(entity.aspects); // add aspects cloning
    }

    public <T extends Aspect> boolean has(Class<T> type) {
        return aspects.containsKey(type);
    }

    public <T extends Aspect> T get(Class<T> type) {
        return (T) aspects.get(type);
    }

    public <T extends Aspect> Optional<T> getOptional(Class<T> type) {
        return (Optional<T>) Optional.of(aspects.get(type));
    }

    public <T extends Aspect> void add(T aspect) {
        if (aspect == null) return;
        aspect.entity = this;
        aspects.put(aspect.getClass(), aspect);
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
