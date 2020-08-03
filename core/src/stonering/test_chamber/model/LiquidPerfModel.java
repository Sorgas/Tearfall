package stonering.test_chamber.model;

import stonering.enums.materials.MaterialMap;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 03.08.2020.
 */
public class LiquidPerfModel extends TestModel {

    public LiquidPerfModel() {
        super(40);
    }

    @Override
    public void init() {
        super.init();
        LiquidContainer container = get(LiquidContainer.class);
        container.createLiquidSource(new Position(1,1, 10), MaterialMap.getId("water"), 1);
    }
}
