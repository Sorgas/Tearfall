package stonering.generators.worldgen.generators;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.objects.local_actors.environment.CelestialBody;
import stonering.objects.local_actors.environment.aspects.CelestialCycleAspect;
import stonering.objects.local_actors.environment.aspects.SelestialLightSource;

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
        return false;
    }

    private void generateSun() {
        CelestialBody sun = new CelestialBody();
        sun.addAspect(new SelestialLightSource(sun));
        float dayScale = 0.0001f;
        sun.addAspect(new CelestialCycleAspect(dayScale, dayScale, sun));
    }

    private void generateMoons() {

    }

    private void generatePlanets() {

    }
}
