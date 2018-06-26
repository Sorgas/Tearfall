package stonering.objects.jobs.actions.aspects.requirements;

import stonering.objects.jobs.actions.Action;

/**
 * @author Alexander Kuzyakov on 03.02.2018.
 *
 * checks if creature has bodypart with specific property
 */
public class BodyPartRequirementAspect extends RequirementsAspect {
    private String bodyPartType;

    public BodyPartRequirementAspect(Action action, String bodyPartType) {
        super(action);
        this.bodyPartType = bodyPartType;
    }

    @Override
    public boolean check() {
        //TODO
        return true;
    }
}
