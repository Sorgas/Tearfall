package stonering.generators.creatures;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;

/**
 * Generates {@link HealthAspect} and fills it with default properties.
 *
 * @author Alexander on 17.09.2019.
 */
public class HealthAspectGenerator {

    public HealthAspect generateHealthAspect(Unit unit) {
        HealthAspect aspect = new HealthAspect(unit);
        aspect.properties.put("performance", (float) 100);
        return aspect;
    }
}
