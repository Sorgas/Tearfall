package stonering.entity.job.action.combat;

import stonering.entity.building.Building;

/**
 * @author Alexander on 04.08.2020.
 */
public abstract class AttackBuildingAction extends AttackAction {
    protected Building targetBuilding;

    public AttackBuildingAction(Building building) {
        super(building);
        targetBuilding = building;
    }
    
    public void giveExperience() {
        //TODO give experience not above lvl 2
    }
}
