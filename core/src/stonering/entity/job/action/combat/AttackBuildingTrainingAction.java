package stonering.entity.job.action.combat;

import stonering.entity.building.Building;
import stonering.entity.job.action.ActionConditionStatusEnum;

/**
 * Action for training in combat with training buildings.
 * 
 * @author Alexander on 04.08.2020.
 */
public class AttackBuildingTrainingAction extends AttackBuildingAction {
    private int strikeCounter = 0;
    
    public AttackBuildingTrainingAction(Building building) {
        super(building);
        
        startCondition = () -> {
            // assigned weapon equipped
            return ActionConditionStatusEnum.OK;
        };
        
        finishCondition = () -> strikeCounter >= 10;
    }

    @Override
    protected void strike() {
        strikeCounter++;
        // no damage
        System.out.println(task.performer + " attacks " + targetBuilding);
        giveExperience();
    }
}
