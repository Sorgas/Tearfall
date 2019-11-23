package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.enums.items.type.ItemTypeMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing farms.
 *
 * @author Alexander_Kuzyakov on 04.07.2019.
 */
public class FarmModel extends TestModel {

    @Override
    public void init() {
        super.init();
        get(EntitySelector.class).setPosition(MAP_SIZE / 2, MAP_SIZE / 2, 2);
        get(UnitContainer.class).addUnit(createUnit());
        get(ItemContainer.class).onMapItemsSystem.putNewItem(createHoe(), new Position(0, 0, 2));
        putSeeds();
    }

    private Unit createUnit() {
        Unit unit = new CreatureGenerator().generateUnit(new Position(3,3,2),"human");
        return unit;
    }

    private Item createHoe() {
        return new Item(null, ItemTypeMap.instance().getItemType("hoe"));
    }

    private void putSeeds() {
        for (int i = 0; i < 4; i++) {
            get(ItemContainer.class).onMapItemsSystem.putNewItem(createSeed(), new Position(1 + i, 0, 2));
        }
    }

    private Item createSeed() {
        Item item = new ItemGenerator().generateSeedItem("farm_test_plant", null);
        return item;
    }
}
