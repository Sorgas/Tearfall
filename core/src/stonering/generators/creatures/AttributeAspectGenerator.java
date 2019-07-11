package stonering.generators.creatures;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.AttributeAspect;

/**
 * Generates {@link AttributeAspect}.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class AttributeAspectGenerator {

    public void generateStats(Unit unit, Unit mother, Unit father) {

    }

    public AttributeAspect generateAttributeAspect(Unit unit) {
        AttributeAspect aspect = new AttributeAspect(null);
        aspect.setAgility(10);
        return aspect;
    }
}
