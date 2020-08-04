package stonering.entity.job.action.combat;

import stonering.entity.unit.Unit;

/**
 * @author Alexander on 04.08.2020.
 */
public class AttackUnitCombatAction extends AttackUnitAction {
    
    public AttackUnitCombatAction(Unit unit) {
        super(unit);
    }

    @Override
    protected void strike() {
        
        giveExperience();
        
    }

    @Override
    protected void doDamage() {
        // TODO do damage to opponent
    }
}
