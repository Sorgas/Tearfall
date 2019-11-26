package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing farms.
 *
 * @author Alexander_Kuzyakov on 04.07.2019.
 */
public class DiggingModel extends TestModel {
    @Override
    public void init() {
        super.init();
        get(EntitySelector.class).position.set(MAP_SIZE / 2, MAP_SIZE / 2, 10);
        get(UnitContainer.class).addUnit(createUnit());
        get(ItemContainer.class).onMapItemsSystem.putNewItem(createPickaxe(), new Position(0, 0, 10));
    }

    @Override
    protected void updateLocalMap() {
        LocalMap localMap = get(LocalMap.class);
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < 10; z++) {
                    localMap.setBlock(x, y, z, BlockTypesEnum.WALL, MaterialMap.instance().getId("soil"));
                }
                localMap.setBlock(x, y, 10, BlockTypesEnum.FLOOR, MaterialMap.instance().getId("soil"));
            }
        }
    }

    private Unit createUnit() {
        return new CreatureGenerator().generateUnit(new Position(3, 3, 10), "human");
    }

    private Item createPickaxe() {
        return new Item(null, ItemTypeMap.getInstance().getItemType("pickaxe"));
    }
}
