package stonering.entity.unit.aspects.body;

import stonering.entity.Aspect;
import stonering.entity.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Points to creature's body, stored in type.
 * Stores wounds on the creature's body, which are individual.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspect extends Aspect {
    public final String bodyTemplate;
    public final List<String> bodyPartsToCover;
    public final List<Wound> wounds;

    public BodyAspect(Unit unit, String bodyTemplate) {
        super(unit);
        this.bodyTemplate = bodyTemplate;
        wounds = new ArrayList<>();
        bodyPartsToCover = new ArrayList<>();
    }
}
