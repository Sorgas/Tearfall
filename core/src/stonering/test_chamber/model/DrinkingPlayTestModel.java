package stonering.test_chamber.model;

import static stonering.enums.unit.health.NeedEnum.THIRST;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.game.model.system.liquid.LiquidTile;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 7/10/2020
 */
public class DrinkingPlayTestModel extends TestModel {

    @Override
    public void init() {
        super.init();
        Unit unit = new CreatureGenerator().generateUnit(new Position(0, 0, 2), "human");
        unit.get(HealthAspect.class).needStates.get(THIRST).setValue(65);
        get(UnitContainer.class).addUnit(unit);

        LocalMap map = get(LocalMap.class);
        map.blockType.set(5, 5, 2, BlockTypeEnum.SPACE);
        map.blockType.set(5, 5, 1, BlockTypeEnum.FLOOR);
        get(LiquidContainer.class).liquidTiles.put(new Position(5, 5, 1), new LiquidTile(MaterialMap.getId("water"), 7));
        ItemContainer itemContainer = get(ItemContainer.class);
    }
}
