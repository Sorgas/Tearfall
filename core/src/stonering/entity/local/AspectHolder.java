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
    protected HashMap<String, Aspect> aspects;

    protected AspectHolder(Position position) {
        this.position = position;
        aspects = new HashMap<>();
    }

    public HashMap<String, Aspect> getAspects() {
        return aspects;
    }

    public void addAspect(Aspect aspect) {
        aspect.setAspectHolder(this);
        aspects.put(aspect.getName(), aspect);
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
