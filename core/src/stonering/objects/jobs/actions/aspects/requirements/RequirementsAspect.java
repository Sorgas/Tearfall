package stonering.objects.jobs.actions.aspects.requirements;

import stonering.objects.jobs.actions.Action;

public abstract class RequirementsAspect {
    protected Action action;

    public abstract boolean check();
}
