package stonering.generators.worldgen.generators;

import stonering.entity.environment.aspects.CelestialLightSourceAspect;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.environment.CelestialBody;
import stonering.entity.environment.aspects.CelestialCycleAspect;

/**
 * Creates celestial bodies like sun, moons, and other planets.
 *
 * @author Alexander Kuzyakov
 */
public class CelestialBodiesGenerator extends WorldGenerator {

    public CelestialBodiesGenerator(WorldGenContainer container) {
        super(container);
    }

    @Override
    public boolean execute() {
        generateSun();
//        generateMoons();
//        generatePlanets();
        return false;
    }

    /**
     * Generates star of star system.
     */
    private void generateSun() {
        CelestialBody sun = new CelestialBody();
        sun.add(new CelestialLightSourceAspect(sun));
        float orbitSpeed = 0.01f;
        sun.add(new CelestialCycleAspect(orbitSpeed, sun));
        container.getWorld().getStarSystem().objects.add(sun);
    }

    /**
     * Generates moons of current planet
     */
    private void generateMoons() {
        CelestialBody moon = new CelestialBody();
        moon.add(new CelestialLightSourceAspect(moon));
        float orbitSpeed = 0.0001f;
        moon.add(new CelestialCycleAspect(orbitSpeed, moon));
        container.getWorld().getStarSystem().objects.add(moon);
    }

    /**
     * Generate other planets of the system.
     */
    private void generatePlanets() {

    }
}
