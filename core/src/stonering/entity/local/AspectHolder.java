package stonering.entity.local;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.IntervalTurnable;
import stonering.util.geometry.Position;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class for all game entity. Contains its aspects.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public abstract class AspectHolder extends IntervalTurnable implements Serializable{
    protected Position position;
    protected HashMap<String, Aspect> aspects;

    protected AspectHolder(Position position) {
        this.aspects = new HashMap<>();
        this.position = position;
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
    public void init(GameContainer container) {
        aspects.forEach((s, aspect) -> aspect.init(container));
    }
}
