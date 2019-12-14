package stonering.entity.world;

import stonering.entity.Entity;
import stonering.entity.environment.CelestialBody;
import stonering.game.model.Updatable;
import stonering.game.model.system.EntityContainer;

import java.io.Serializable;

/**
 * Represents star system. Updates every in-game minute. Used for deep calendar, eclipses, mood phases (post MVP).
 * //TODO implement
 *
 * @author Alexander Kuzyakov
 */
public class StarSystem extends EntityContainer<CelestialBody> implements Serializable, Updatable {

    public void init() {
        entities.forEach(Entity::init);
    }
}
