package stonering.test_chamber.model;

import stonering.enums.materials.MaterialMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidFlowPlaytestModel extends TestModel {
    private LocalMap localMap;
    private LiquidContainer container;

    @Override
    public void init() {
        super.init();
        localMap = get(LocalMap.class);
        container = get(LiquidContainer.class);
        // waterfall
        new Int3dBounds(0, 6, 2, 6, 10, 6).iterate(this::setWall); // hill
        // bed on the hill
        new Int3dBounds(0, 8, 6, 6, 8, 6).iterate(this::setFloor);
        new Int3dBounds(0, 8, 7, 6, 8, 7).iterate(this::setSpace);
        // pond
        new Int3dBounds(7, 7, 2, 10, 9, 2).iterate(this::setFloor);
        new Int3dBounds(7, 7, 3, 10, 9, 4).iterate(this::setSpace);
        container.createLiquidSource(new Position(0, 8, 6), MaterialMap.getId("water"), 1);


        // U-tube
        new Int3dBounds(1, 0, 0, 9, 0, 0).iterate(this::setFloor); // horizontal
//        new Int3dBounds(1, 0, 1, 9, 0, 1).iterate(this::setSpace);
        new Int3dBounds(1, 0, 1, 1, 0, 4).iterate(this::setSpace); // vertical
        new Int3dBounds(9, 0, 1, 9, 0, 4).iterate(this::setSpace); // vertical
        new Int3dBounds(5, 0, 4, 5, 6, 5).iterate(this::setWall); // barrier
        container.createLiquidSource(new Position(1, 0, 5), MaterialMap.getId("water"), 1);
        get(EntitySelectorSystem.class).setSelectorPosition(new Position(5, 5, 6));
    }

    @Override
    protected void updateLocalMap() {
        new Int3dBounds(0, 0, 0, MAP_SIZE - 1, MAP_SIZE - 1, 3).iterate(this::setWall);
        new Int3dBounds(0, 0, 4, MAP_SIZE - 1, MAP_SIZE - 1, 4).iterate(this::setFloor);
    }
}
