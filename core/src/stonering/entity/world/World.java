package stonering.entity.world;

import stonering.entity.local.environment.CelestialBody;
import stonering.generators.worldgen.WorldMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the whole game world. Includes map of current planet,
 * and descriptor objects for other bodies in the system.
 * //TODO implement star system class. This is for calendar events, eclipses, rituals, holidays etc.
 *
 * @author Alexander Kuzyakov
 */
public class World implements Serializable {
    private WorldMap worldMap;
    private List<CelestialBody> celestialBodies;
    private int seed;

    public World(int width, int height) {
        worldMap = new WorldMap(width, height);
        celestialBodies = new ArrayList<>();
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public List<CelestialBody> getCelestialBodies() {
        return celestialBodies;
    }
}
