package stonering.generators.creatures;

import stonering.enums.unit.CreatureType;
import stonering.entity.unit.aspects.body.BodyAspect;

/**
 * Generates {@link BodyAspect} for creature by given species.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspectGenerator {

    public BodyAspect generateBodyAspect(CreatureType type) {
        BodyAspect aspect = new BodyAspect(null, type.bodyTemplate.name);
        aspect.bodyPartsToCover.addAll(type.limbsToCover);
        return aspect;
    }
}
