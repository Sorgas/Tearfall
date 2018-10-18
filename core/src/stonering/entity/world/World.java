package stonering.entity.world;

import java.io.Serializable;

/**
 * Represents the whole game world. Includes map of current planet,
 * and descriptor objects for other bodies in the system.
 * //TODO implement star system class. This is for calendar events, eclipses, rituals, holidays etc.
 *
 * @author Alexander Kuzyakov
 */
public class World implements Serializable {
    private WorldMap worldMap;
    private StarSystem starSystem;
    private int seed;

    public World(int width, int height) {
        worldMap = new WorldMap(width, height);
        starSystem = new StarSystem();
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public StarSystem getStarSystem() {
        return starSystem;
    }
}
