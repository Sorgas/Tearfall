package stonering.generators.creatures;

import stonering.enums.unit.CreatureType;
import stonering.entity.local.unit.aspects.body.BodyAspect;

/**
 * Generates {@link BodyAspect} for creature by given species.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspectGenerator {

    public BodyAspect generateBodyAspect(CreatureType type) {
        BodyAspect aspect = new BodyAspect(null);
        aspect.setBodyTemplate(type.bodyTemplate.name);
        aspect.setBodyPartsToCover(type.limbsToCover);
        return aspect;
    }
}
