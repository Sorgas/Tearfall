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
    private String bodyTemplate;
    private List<String> bodyPartsToCover;
    private List<Wound> wounds;

    public BodyAspect(Unit unit) {
        super(unit);
        wounds = new ArrayList<>();
        bodyPartsToCover = new ArrayList<>();
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public void setBodyPartsToCover(List<String> bodyPartsToCover) {
        this.bodyPartsToCover = bodyPartsToCover;
    }

    public List<String> getBodyPartsToCover() {
        return bodyPartsToCover;
    }
}
