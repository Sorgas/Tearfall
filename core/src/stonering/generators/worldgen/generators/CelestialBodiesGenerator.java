package stonering.generators.worldgen.generators;

import stonering.entity.environment.aspects.CelestialLightSourceAspect;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.environment.CelestialBody;
import stonering.entity.environment.aspects.CelestialCycleAspect;

/**
 * Creates celestial bodies like sun, moons, and other planets.
 * TODO main planet should have moons, lighting it during nights and having phases
 * TODO eclipses
 * TODO sky clocks(astrolabia) widget, allowing player to see positions and phases of celestial bodies
 * TODO moons affect magic and creatures?
 *
 * @author Alexander Kuzyakov
 */
public class CelestialBodiesGenerator extends WorldGenerator {

    @Override
    public void set(WorldGenContainer container) {

    }

    @Override
    public void run() {
        generateSun();
        generateMainPlanet();

    }

    /**
     * Generates star of star system.
     */
    private void generateSun() {
        CelestialBody sun = new CelestialBody();
        sun.add(new CelestialLightSourceAspect());
        float orbitSpeed = 0.01f;
        sun.add(new CelestialCycleAspect(orbitSpeed, sun));
        container.world.starSystem.objects.add(sun);
    }

    /**
     * Generates moons of current planet
     */
    private void generateMoons() {
        CelestialBody moon = new CelestialBody();
        moon.add(new CelestialLightSourceAspect(moon));
        float orbitSpeed = 0.0001f;
        moon.add(new CelestialCycleAspect(orbitSpeed, moon));
        container.world.starSystem.objects.add(moon);
    }

    /**
     * Generate other planets of the system.
     */
    private void generatePlanets() {

    }

    private void generateMainPlanet() {

    }
}
