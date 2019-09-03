package stonering.entity;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.IntervalTurnable;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class for all game entities. Contains its aspects.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public abstract class Entity extends IntervalTurnable implements Serializable, Initable {
    protected HashMap<Class, Aspect> aspects;

    protected Entity() {
        aspects = new HashMap<>();
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
    public void turnInterval(TimeUnitEnum unit) {
        aspects.values().forEach(aspect -> aspect.turnInterval(unit));
    }

    @Override
    public void init() {
        for (Aspect aspect : aspects.values()) {
            if(aspect instanceof Initable) ((Initable) aspect).init();
        }
    }
}
