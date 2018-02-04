package stonering.objects.jobs.actions.aspects.requirements;

import stonering.objects.jobs.actions.Action;

public abstract class RequirementsAspect {
    protected Action action;

    public RequirementsAspect(Action action) {
        this.action = action;
    }

    public abstract boolean check();
}
