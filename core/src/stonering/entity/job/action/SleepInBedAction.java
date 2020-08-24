package stonering.entity.job.action;

import stonering.entity.RenderAspect;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.job.action.plant.ChopTreeAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.need.NeedEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for sleeping. Progress in this action replenishes tiredness.
 * This action continues till creature is fully rested, instead of fixed length as for {@link ChopTreeAction}.
 * TODO Creature will first lie without sleep being able to see the surroundings, and then sleep with closed eyes. Lengths of both phases are influenced by creature
 *
 * @author Alexander on 10.09.2019.
 */
public class SleepInBedAction extends Action {
    private float restSpeed;
    private NeedState fatigue;

    public SleepInBedAction(Building bed) {
        super(new EntityActionTarget(bed, ActionTargetTypeEnum.EXACT));

        restSpeed = countRestSpeed();
        startCondition = () -> {
            if (GameMvc.model().get(BuildingContainer.class).getBuilding(bed.position) == bed
                    && bed.has(RestFurnitureAspect.class))
                return OK;
            return FAIL;
        };
        onStart = () -> {
            fatigue = task.performer.get(NeedAspect.class).needs.get(NeedEnum.REST);
            task.performer.get(RenderAspect.class).rotation = -90;
            // lie to bed
            // disable vision
            // decrease hearing
        };
        progressConsumer = (delta) -> {
            fatigue.changeValue(-delta); // decrease fatigue
        };
        finishCondition = () -> fatigue.current() <= 0; // stop sleeping
        onFinish = () -> {
            // restore vision and hearing
            task.performer.get(RenderAspect.class).rotation = 0;
            // if fatigue is not restored completely, apply negative mood buff
        };
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
