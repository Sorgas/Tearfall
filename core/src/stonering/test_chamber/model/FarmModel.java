package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.enums.items.type.ItemTypeMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
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
        get(ItemContainer.class).addItem(createHoe());
    }

    private Unit createUnit() {
        Unit unit = new CreatureGenerator().generateUnit("human");
        unit.setPosition(new Position(3, 3, 2));
        return unit;
    }

    private Item createHoe() {
        Item item = new Item(null, ItemTypeMap.getInstance().getItemType("hoe"));
        item.setPosition(new Position(0, 0, 2));
        return item;
    }
}
