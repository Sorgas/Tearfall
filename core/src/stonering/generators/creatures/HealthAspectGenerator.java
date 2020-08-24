package stonering.generators.creatures;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.enums.unit.health.HealthFunctionValue;

/**
 * Generates {@link HealthAspect} and fills it with default properties.
 *
 * @author Alexander on 17.09.2019.
 */
public class HealthAspectGenerator {

    public HealthAspect generateHealthAspect(Unit unit) {
        HealthAspect aspect = new HealthAspect(unit);
        for (HealthFunctionEnum value : HealthFunctionEnum.values()) {
            aspect.functions.put(value, new HealthFunctionValue(1f));
        }
        aspect.properties.put("performance", 0f);
        aspect.properties.put("hp", 0f);
        return aspect;
    }
}
