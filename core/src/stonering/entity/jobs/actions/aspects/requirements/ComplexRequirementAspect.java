package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.jobs.actions.Action;

import java.util.Arrays;
import java.util.List;

/**
 * Requirement that is combination of some simple requirements.
 * All simple requirements should give true on check to pass this (&& like).
 *
 * @author Alexander on 29.08.2018.
 */
public class ComplexRequirementAspect extends RequirementsAspect {
    private List<RequirementsAspect> aspects;

    public ComplexRequirementAspect(Action action) {
        super(action);
    }

    public ComplexRequirementAspect(Action action, List<RequirementsAspect> aspects) {
        super(action);
        this.aspects = aspects;
    }

    public ComplexRequirementAspect(Action action, RequirementsAspect[] aspects) {
        this(action, Arrays.asList(aspects));
    }

    @Override
    public boolean check() {
        for (RequirementsAspect aspect : aspects) {
            if (!aspect.check()) {
                return false;
            }
        }
        return true;
    }

    public void addRequirementAspect(RequirementsAspect aspect) {
        aspects.add(aspect);
    }
}
