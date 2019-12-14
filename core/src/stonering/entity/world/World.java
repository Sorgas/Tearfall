package stonering.entity.world;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Updatable;
import stonering.game.model.system.ModelComponent;
import stonering.util.global.Initable;

import java.io.Serializable;

/**
 * Represents the whole game world. Includes map of current planet,
 * and descriptor objects for other bodies in the system.
 * //TODO implement star system class. This is for calendar events, eclipses, rituals, holidays etc.
 *
 * @author Alexander Kuzyakov
 */
public class World implements Serializable, Initable, ModelComponent, Updatable {
    private WorldMap worldMap;
    private StarSystem starSystem;
    private int seed;

    public World(int width, int height) {
        worldMap = new WorldMap(width, height);
        starSystem = new StarSystem();
    }

    @Override
    public void init() {
        starSystem.init();
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public StarSystem getStarSystem() {
        return starSystem;
    }

    @Override
    public void update(TimeUnitEnum unit) {
        if (unit == TimeUnitEnum.HOUR) starSystem.update(unit);
    }
}
