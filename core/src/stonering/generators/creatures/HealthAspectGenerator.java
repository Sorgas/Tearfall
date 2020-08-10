package stonering.generators.creatures;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.need.NeedState;

import static stonering.enums.unit.health.NeedEnum.*;
import static stonering.enums.unit.health.NeedEnum.THIRST;

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
        aspect.needStates.put(FATIGUE, generateStateForFatigue());
        aspect.needStates.put(HUNGER, new NeedState(HUNGER));
        aspect.needStates.put(THIRST, new NeedState(THIRST));
        return aspect;
    }

    public NeedState generateStateForFatigue() {
        //TODO adjust max fatigue
        return new NeedState(FATIGUE);
    }
}
