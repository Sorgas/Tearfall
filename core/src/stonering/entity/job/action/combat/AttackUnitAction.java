package stonering.entity.job.action.combat;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;

/**
 * Action for attacking other unit with melee weapon.
 * First strike occurs immediately.
 * After strike attackDelay is set, based on weapon speed; and progress is set based on unit skill and health condition.
 * 
 * MaxProgress is not used in this action.
 * 
 * @author Alexander on 04.08.2020.
 */
public abstract class AttackUnitAction extends AttackAction {
    protected Unit targetUnit;
    
    public AttackUnitAction(Unit unit) {
        super(unit);  
        targetUnit = unit;
        
        finishCondition = () -> !targetUnit.get(HealthAspect.class).alive;
    }

    @Override
    protected void strike() {
        // select protection -> do damage, give experience
    }

    protected void giveExperience() {
        // TODO give experience to performer and target
    }
    
    protected abstract void doDamage();
}
