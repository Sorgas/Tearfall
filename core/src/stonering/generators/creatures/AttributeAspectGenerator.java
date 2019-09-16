package stonering.generators.creatures;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.AttributeAspect;
import stonering.enums.unit.Attributes;

import static stonering.enums.unit.Attributes.*;

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
        aspect.attributes.put(AGILITY, 10);
        aspect.attributes.put(ENDURANCE, 10);
        return aspect;
    }
}
