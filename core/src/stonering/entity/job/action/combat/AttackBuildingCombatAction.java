package stonering.entity.job.action.combat;

import stonering.entity.building.Building;

/**
 * Action for attacking hostile buildings.
 * 
 * @author Alexander on 04.08.2020.
 */
public class AttackBuildingCombatAction extends AttackBuildingAction {

    public AttackBuildingCombatAction(Building building) {
        super(building);
    }

    @Override
    protected void strike() {
        // TODO do damage to building.
        giveExperience();
    }
}
