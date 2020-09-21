package stonering.test_chamber.model;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.unit.need.NeedEnum;
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
        unit.get(NeedAspect.class).needs.get(NeedEnum.WATER).setValue(1);
        get(UnitContainer.class).add(unit);

        LocalMap map = get(LocalMap.class);
        map.blockType.set(5, 5, 2, BlockTypeEnum.SPACE);
        map.blockType.set(5, 5, 1, BlockTypeEnum.FLOOR);
        get(LiquidContainer.class).liquidTiles.put(new Position(5, 5, 1), new LiquidTile(MaterialMap.getId("water"), 7));
        ItemContainer itemContainer = get(ItemContainer.class);
    }
}
