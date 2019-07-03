package stonering.test_chamber.model;

import stonering.entity.local.unit.Unit;
import stonering.game.model.lists.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;

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
        Unit unit = new CreatureGenerator().generateUnit("human");
        unit.getPosition().set(getMapSize() / 2, getMapSize() / 2, 2);
        return unit;
    }
}
