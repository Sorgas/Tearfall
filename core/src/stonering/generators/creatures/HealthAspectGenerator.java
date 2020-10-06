package stonering.generators.creatures;

import java.util.Arrays;
import java.util.Random;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.unit.health.CreatureAttributeEnum;
import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.enums.unit.health.GameplayStatEnum;

/**
 * Generates {@link HealthAspect} and fills it with default properties.
 *
 * @author Alexander on 17.09.2019.
 */
public class HealthAspectGenerator {
    Random random = new Random();

    public HealthAspect generateHealthAspect(Unit unit) {
        HealthAspect aspect = new HealthAspect();
        Arrays.stream(HealthFunctionEnum.values()).forEach(value -> aspect.functions.put(value, 1f));
        for (CreatureAttributeEnum attribute : CreatureAttributeEnum.values()) {
            int value = 3 + random.nextInt(4);
            aspect.baseAttributes.put(attribute, value);
            aspect.attributes.put(attribute, value);
        }
        for (GameplayStatEnum stat : GameplayStatEnum.values()) {
            aspect.baseStats.putAll(unit.type.statMap); // copy base values from type
            // TODO modify base values by traits.
            aspect.update(stat);
        }
        return aspect;
    }
}
