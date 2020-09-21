package stonering.test_chamber.model;

import stonering.entity.building.Building;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.OrientationEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.need.NeedEnum;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.buildings.BuildingGenerator;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 6/14/2020
 */
public class FoodPlayTestModel extends TestModel {
    private ItemGenerator itemGenerator;

    @Override
    public void init() {
        super.init();
        Unit unit = new CreatureGenerator().generateUnit(new Position(0, 0, 2), "human");
        unit.get(NeedAspect.class).needs.get(NeedEnum.FOOD).setValue(65);
        get(UnitContainer.class).add(unit);

        ItemContainer itemContainer = get(ItemContainer.class);
        BuildingContainer buildingContainer = get(BuildingContainer.class);
        itemGenerator = new ItemGenerator();
        BuildingGenerator buildingGenerator = new BuildingGenerator();
        
        Item rawMeat = itemGenerator.generateItem("meat_piece", "meat", null);

        Item spoiledMeat = itemGenerator.generateItem("meat_piece", "meat", null);
        spoiledMeat.tags.add(ItemTagEnum.SPOILED);

        Item preparedMeat = itemGenerator.generateItem("meat_piece", "meat", null);
        preparedMeat.tags.add(ItemTagEnum.PREPARED);
        preparedMeat.tags.remove(ItemTagEnum.RAW);

        Building chest = buildingGenerator.generateBuilding("chest", new Position(8, 3, 2), OrientationEnum.N);
        buildingContainer.addBuilding(chest);
        
        itemContainer.onMapItemsSystem.addNewItemToMap(rawMeat, new Position(6, 1, 2));
        itemContainer.onMapItemsSystem.addNewItemToMap(spoiledMeat, new Position(7, 1, 2));
        itemContainer.addItem(preparedMeat);
        itemContainer.containedItemsSystem.addItemToContainer(preparedMeat, chest.get(ItemContainerAspect.class));
        itemContainer.onMapItemsSystem.addNewItemToMap(preparedMeat, new Position(8, 1, 2));
    }
}
