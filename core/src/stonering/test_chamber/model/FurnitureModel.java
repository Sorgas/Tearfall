package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.enums.OrientationEnum;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.buildings.BuildingGenerator;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing furniture buildings.
 *
 * @author Alexander on 22.09.2019.
 */
public class FurnitureModel extends TestModel {
    private BuildingGenerator generator;

    @Override
    public void init() {
        super.init();
        generator = new BuildingGenerator();
        get(EntitySelectorSystem.class).selector.position.set(MAP_SIZE / 2, MAP_SIZE / 2, 2);
        get(UnitContainer.class).add(createUnit());
        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(createItem(), new Position(7, 7, 2));
        get(BuildingContainer.class).addBuilding(generator.generateBuilding("kitchen", new Position(0, 4, 2), OrientationEnum.N));
        get(BuildingContainer.class).addBuilding(generator.generateBuilding("kitchen", new Position(3, 4, 2), OrientationEnum.S));
        get(BuildingContainer.class).addBuilding(generator.generateBuilding("kitchen", new Position(6, 4, 2), OrientationEnum.E));
        get(BuildingContainer.class).addBuilding(generator.generateBuilding("kitchen", new Position(9, 4, 2), OrientationEnum.W));
    }

    private Unit createUnit() {
        Unit unit = new CreatureGenerator().generateUnit(new Position(3, 3, 2), "human");
        return unit;
    }

    private Item createItem() {
        return new ItemGenerator().generateItem("pants", "cotton", null);
    }
}
