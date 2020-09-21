package stonering.test_chamber.model;

import stonering.entity.building.Building;
import stonering.entity.job.Task;
import stonering.entity.job.action.combat.AttackBuildingTrainingAction;
import stonering.enums.OrientationEnum;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.buildings.BuildingGenerator;
import stonering.generators.creatures.CreatureGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 03.08.2020.
 */
public class AttackPlayTestModel extends TestModel {
    
    @Override
    public void init() {
        super.init();
        CreatureGenerator generator = new CreatureGenerator();
        get(UnitContainer.class).add(generator.generateUnit(new Position(1, 5, 2), "human"));
        Building dummy = new BuildingGenerator().generateBuilding("training_dummy", new Position(6, 5, 2), OrientationEnum.N);
        get(BuildingContainer.class).addBuilding(dummy);
        get(TaskContainer.class).addTask(new Task(new AttackBuildingTrainingAction(dummy)));
    }
}
