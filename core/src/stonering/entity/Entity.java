package stonering.entity;

import stonering.game.model.EntityIdGenerator;
import stonering.util.geometry.Position;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Class for all game entities. Contains its aspects.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public abstract class Entity implements Serializable, Cloneable {
    public final int id;
    public final HashMap<Class, Aspect> aspects;
    public Position position;

    protected Entity() {
        aspects = new HashMap<>();
        id = EntityIdGenerator.get();
    }

    public Entity(Position position) {
        this();
        this.position = position != null ? position.clone() : null;
    }

    public <T extends Aspect> boolean has(@Nonnull Class<T> type) {
        return aspects.containsKey(type);
    }

    public <T extends Aspect> T get(@Nonnull Class<T> type) {
        return (T) aspects.get(type);
    }

    public <T extends Aspect> Optional<T> optional(Class<T> type) {
        return Optional.ofNullable(get(type));
    }

    public <T extends Aspect> void add(T aspect) {
        if (aspect == null) return;
        aspect.entity = this;
        aspects.put(aspect.getClass(), aspect);
    }

    public <T extends Aspect> void remove(@Nonnull Class<T> type) {
        optional(type).ifPresent(aspect -> {
            aspect.entity = null;
            aspects.remove(type);
        });
    }

    public void setPosition(Position position) {
        if (this.position == null) this.position = new Position();
        this.position.set(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
