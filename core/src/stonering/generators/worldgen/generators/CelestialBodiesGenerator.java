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
public class CelestialBodiesGenerator extends AbstractGenerator {

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
        sun.addAspect(new CelestialLightSourceAspect(sun));
        float dayScale = 0.01f;
        sun.addAspect(new CelestialCycleAspect(dayScale, dayScale, sun));
        container.getWorld().getStarSystem().getCelestialBodies().add(sun);
    }

    /**
     * Generates moons of current planet
     */
    private void generateMoons() {
        CelestialBody moon = new CelestialBody();
        moon.addAspect(new CelestialLightSourceAspect(moon));
        float dayScale = 0.0001f;
        moon.addAspect(new CelestialCycleAspect(dayScale, dayScale, moon));
        container.getWorld().getStarSystem().getCelestialBodies().add(moon);
    }

    /**
     * Generate other planets of the system.
     */
    private void generatePlanets() {

    }
}
