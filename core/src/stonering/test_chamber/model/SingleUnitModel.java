package stonering.test_chamber.model;

import stonering.entity.unit.Unit;
import stonering.game.model.lists.units.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander_Kuzyakov on 03.07.2019.
 */
public class SingleUnitModel extends TestModel {
    @Override
    public void init() {
        super.init();
        get(UnitContainer.class).addUnit(createUnit());
    }

    private Unit createUnit() {
        return new CreatureGenerator().generateUnit(new Position(getMapSize() / 2, getMapSize() / 2, 2), "human");
    }
}
