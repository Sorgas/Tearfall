package stonering.entity.job.action.sleep_action;

import stonering.entity.building.Building;
import stonering.entity.job.action.PhasedAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.TreatsAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.unit.TreatEnum;
import stonering.enums.unit.health.HealthParameterEnum;

/**
 * Action for sleeping.
 * TODO
 * @author Alexander on 10.09.2019.
 */
public class SleepInBedAction extends PhasedAction {
    Building bed;
    private float restSpeed;

    public SleepInBedAction(ActionTarget actionTarget) {
        super(actionTarget);
        bed = (Building) ((EntityActionTarget) actionTarget).entity;
        restSpeed = countRestSpeed();
    }

    @Override
    protected void recreatePhases() {
        phases.add(new RestPhase()); // 5-240 min
        //      +                 -
        // pain               tiredness

        phases.add(new SleepPhase()); // 30-720 min
        //      +                 -
        // personal treat      pain
        // illness
        // tiredness
    }

    @Override
    public int check() {
        return FAIL;
    }

    @Override
    protected void performLogic() {
        // all logic in last phase
    }

    @Override
    protected float getWorkDelta() {
        return restSpeed;
    }

    private float countRestSpeed() {
//        TreatsAspect aspect = task.performer.getAspect(TreatsAspect.class);
//        if(aspect != null && aspect.treats.contains(TreatEnum.FAST_REST)) speed *= 1.1f; // +10%
//        else if(aspect != null && aspect.treats.contains(TreatEnum.SLOW_REST)) speed *= 0.9f; // +10%
        //TODO consider bed quality and treats
        return 0.003125f; // applied every tick. gives 90 points over 8 hours
    }

    private float getRequiredRestAmount() {
        return 0;
    }

    /**
     * Gets length of sleep (in work units). High tiredness, being ill
     */
    private float getRequiredSleepAmount() {
        return task.performer.getAspect(HealthAspect.class).parameters.get(HealthParameterEnum.FATIGUE).current / restSpeed;

    }
}
