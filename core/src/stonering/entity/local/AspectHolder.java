package stonering.entity.local;

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
public abstract class AspectHolder extends IntervalTurnable implements Serializable, Initable {
    protected Position position; //TODO move to PositionAspect
    protected HashMap<Class, Aspect> aspects;

    protected AspectHolder(Position position) {
        this.position = position;
        aspects = new HashMap<>();
    }

    public <T extends Aspect> T getAspect(Class<T> type) {
        return (T) aspects.get(type);
    }

    public <T extends Aspect> void addAspect(T aspect) {
        if (aspect == null) return;
        aspect.setAspectHolder(this);
        aspects.put(aspect.getClass(), aspect);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void turn() {
        aspects.values().forEach(Aspect::turn);
    }

    /**
     * Inits aspects of building.
     */
    @Override
    public void init() {
        aspects.forEach((s, aspect) -> aspect.init());
    }
}
