package stonering.entity.job.action.combat;

import static stonering.entity.job.action.ActionConditionStatusEnum.OK;
import static stonering.enums.action.ActionTargetTypeEnum.NEAR;

import stonering.entity.Entity;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.EntityActionTarget;

/**
 * @author Alexander on 04.08.2020.
 */
public abstract class AttackAction extends Action {
    protected float attackDelay;
    
    public AttackAction(Entity entity) {
        super(new EntityActionTarget(entity, NEAR));
        progress = 1;
        
        startCondition = () -> OK;

        progressConsumer = (progress) -> {
            attackDelay -= progress * getUnitPerformance();
            if(attackDelay > 0) return;
            strike();
            updateStrikeDelay();
        };
    }

    /**
     * Defines how the damage is dealt.
     */
    protected abstract void strike();

    private float getUnitPerformance() {
        return 1; // TODO get unit skill and performance
    }

    private void updateStrikeDelay() {
        attackDelay = 10; // TODO get weapon speed
    }
}
