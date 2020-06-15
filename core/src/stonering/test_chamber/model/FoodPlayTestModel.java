package stonering.test_chamber.model;

import static stonering.enums.unit.health.HealthParameterEnum.HUNGER;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.items.ItemTagEnum;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 6/14/2020
 */
public class FoodPlayTestModel extends TestModel {

    @Override
    public void init() {
        super.init();
        Unit unit = new CreatureGenerator().generateUnit(new Position(0, 0, 2), "human");
        unit.get(HealthAspect.class).parameters.get(HUNGER).setValue(65);
        get(UnitContainer.class).addUnit(unit);

        ItemContainer container = get(ItemContainer.class);

        ItemGenerator generator = new ItemGenerator();

        Item rawMeat = generator.generateItem("meat_piece", "meat", null);

        Item spoiledMeat = generator.generateItem("meat_piece", "meat", null);
        spoiledMeat.tags.add(ItemTagEnum.SPOILED);

        Item preparedMeat = generator.generateItem("meat_piece", "meat", null);
        preparedMeat.tags.add(ItemTagEnum.PREPARED);
        preparedMeat.tags.remove(ItemTagEnum.RAW);

        container.onMapItemsSystem.putNewItem(rawMeat, new Position(6, 1, 2));
        container.onMapItemsSystem.putNewItem(spoiledMeat, new Position(7, 1, 2));
        container.onMapItemsSystem.putNewItem(preparedMeat, new Position(8, 1, 2));
    }
}
