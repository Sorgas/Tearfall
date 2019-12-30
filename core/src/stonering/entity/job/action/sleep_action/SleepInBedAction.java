package stonering.entity.job.action.sleep_action;

import stonering.entity.building.Building;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.phase.PhasedAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for sleeping. Creature will first lie without sleep being able to see the surroundings,
 * and then sleep with closed eyes. Lengths of both phases are influenced by creature
 * TODO
 *
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
        phases.add(new RestPhase(this, getRequiredRestLength())); // 5-240 min
        phases.add(new SleepPhase(this, getMaxSleepLength())); // 30-720 min
    }

    @Override
    public ActionConditionStatusEnum check() {
        if(GameMvc.instance().model().get(BuildingContainer.class).getBuiding(bed.position) == bed &&
                bed.hasAspect(RestFurnitureAspect.class))
            return OK;
        return FAIL;
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

    /**
     * Creatures should rest before sleeping.
     * Having bad mood, pain or other negative conditions will prevent sleeping.
     */
    private int getRequiredRestLength() {
        return 0; // TODO
    }

    /**
     * Creatures cannot sleep more than 12 hours. Being sick or having an injury will decrease sleep length.
     * TODO consider sickness and injures.
     */
    private int getMaxSleepLength() {
        return 12 * TimeUnitEnum.HOUR.SIZE * TimeUnitEnum.MINUTE.SIZE; // 12 hours max
    }
}
