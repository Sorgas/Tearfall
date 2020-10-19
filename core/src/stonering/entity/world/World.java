package stonering.entity.world;

import stonering.enums.time.TimeUnitEnum;
import stonering.util.lang.Updatable;
import stonering.game.model.system.ModelComponent;

import java.io.Serializable;

/**
 * Represents the whole game world. Includes map of current planet,
 * and descriptor objects for other bodies in the system.
 * //TODO implement star system class. This is for calendar events, eclipses, rituals, holidays etc.
 *
 * @author Alexander Kuzyakov
 */
public class World implements Serializable, ModelComponent, Updatable {
    public String name;
    public final WorldMap worldMap;
    public final StarSystem starSystem;
    public final int seed;

    public World(int width, int height, int seed) {
        worldMap = new WorldMap(width, height);
        this.seed = seed;
        starSystem = new StarSystem();
        name = "";
    }

    @Override
    public void update(TimeUnitEnum unit) {
        if (unit == TimeUnitEnum.HOUR) starSystem.update(unit);
    }
}