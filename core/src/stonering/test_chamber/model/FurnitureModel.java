package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing furniture buildings.
 *
 * @author Alexander on 22.09.2019.
 */
public class FurnitureModel extends TestModel {

    @Override
    public void init() {
        super.init();
        get(EntitySelector.class).position.set(MAP_SIZE / 2, MAP_SIZE / 2, 2);
        get(UnitContainer.class).addUnit(createUnit());
        get(ItemContainer.class).onMapItemsSystem.putNewItem(createItem(), new Position(7,7,2));
    }

    private Unit createUnit() {
        Unit unit = new CreatureGenerator().generateUnit(new Position(3,3,2),"human");
        return unit;
    }

    private Item createItem() {
        return new ItemGenerator().generateItem("pants", "cotton", null);
    }
}
