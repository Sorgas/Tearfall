package stonering.entity.job.action.combat;

import stonering.entity.unit.Unit;

/**
 * @author Alexander on 04.08.2020.
 */
public class AttackUnitTrainingAction extends AttackUnitAction {
    
    public AttackUnitTrainingAction(Unit unit) {
        super(unit);
    }

    @Override
    protected void strike() {
        // no damage
        giveExperience();
    }

    @Override
    protected void doDamage() {
        // no damage
    }
}
