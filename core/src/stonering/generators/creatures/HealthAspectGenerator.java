package stonering.generators.creatures;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthParameterState;

import static stonering.enums.unit.health.HealthParameterEnum.*;
import static stonering.enums.unit.health.HealthParameterEnum.THIRST;

/**
 * Generates {@link HealthAspect} and fills it with default properties.
 *
 * @author Alexander on 17.09.2019.
 */
public class HealthAspectGenerator {

    public HealthAspect generateHealthAspect(Unit unit) {
        HealthAspect aspect = new HealthAspect(unit);
        aspect.properties.put("performance", 0f);
        aspect.properties.put("hp", 0f);
        aspect.parameters.put(FATIGUE, generateStateForFatigue());
        aspect.parameters.put(HUNGER, new HealthParameterState(HUNGER));
        aspect.parameters.put(THIRST, new HealthParameterState(THIRST));
        return aspect;
    }

    public HealthParameterState generateStateForFatigue() {
        //TODO adjust max fatigue
        return new HealthParameterState(FATIGUE);
    }
}
