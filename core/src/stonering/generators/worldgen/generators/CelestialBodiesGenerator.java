package stonering.generators.worldgen.generators;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.local.environment.CelestialBody;
import stonering.entity.local.environment.aspects.CelestialCycleAspect;
import stonering.entity.local.environment.aspects.SelestialLightSource;

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
        generateMoons();
        generatePlanets();

        return false;
    }

    /**
     * Generates star of star system.
     */
    private void generateSun() {
        CelestialBody sun = new CelestialBody();
        sun.addAspect(new SelestialLightSource(sun));
        float dayScale = 0.0001f;
        sun.addAspect(new CelestialCycleAspect(dayScale, dayScale, sun));
        container.getCelestialBodies().add(sun);
    }

    /**
     * Generates moons of current planet
     */
    private void generateMoons() {
        CelestialBody moon = new CelestialBody();
        moon.addAspect(new SelestialLightSource(moon));
        float dayScale = 0.0001f;
        moon.addAspect(new CelestialCycleAspect(dayScale, dayScale, moon));
        container.getCelestialBodies().add(moon);
    }

    /**
     * Generate other planets of the system.
     */
    private void generatePlanets() {

    }
}
