package stonering.generators.creatures;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.StatAspect;

import static stonering.enums.unit.CreatureAttributesEnum.*;

/**
 * Generates {@link StatAspect}.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class AttributeAspectGenerator {

    public void generateStats(Unit unit, Unit mother, Unit father) {

    }

    public StatAspect generateAttributeAspect(Unit unit) {
        StatAspect aspect = new StatAspect(null);
        aspect.stats.put(AGILITY, 10);
        aspect.stats.put(ENDURANCE, 10);
        return aspect;
    }
}
