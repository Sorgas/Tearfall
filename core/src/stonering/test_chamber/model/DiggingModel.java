package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.items.type.ItemTypeMap;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
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
        get(EntitySelector.class).setPosition(MAP_SIZE / 2, MAP_SIZE / 2, 10);
        get(UnitContainer.class).addUnit(createUnit());
        get(ItemContainer.class).addItem(createHoe());
    }

    @Override
    protected LocalMap createLocalMap(int size) {
        LocalMap localMap = new LocalMap(size, size, 20);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < 10; z++) {
                    localMap.setBlock(x, y, z, BlockTypesEnum.WALL, MaterialMap.instance().getId("soil"));
                }
                localMap.setBlock(x, y, 10, BlockTypesEnum.FLOOR, MaterialMap.instance().getId("soil"));
            }
        }
        return localMap;
    }

    private Unit createUnit() {
        Unit unit = new CreatureGenerator().generateUnit("human");
        unit.setPosition(new Position(3, 3, 10));
        return unit;
    }

    private Item createHoe() {
        Item item = new Item(null, ItemTypeMap.getInstance().getItemType("pickaxe"));
        item.setPosition(new Position(0, 0, 10));
        return item;
    }
}
