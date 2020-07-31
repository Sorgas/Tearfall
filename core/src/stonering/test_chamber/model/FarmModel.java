package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.enums.ZoneTypeEnum;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;

/**
 * Model for testing farms.
 *
 * @author Alexander_Kuzyakov on 04.07.2019.
 */
public class FarmModel extends TestModel {
    private ItemGenerator generator = new ItemGenerator();

    @Override
    public void init() {
        super.init();
        get(EntitySelectorSystem.class).selector.position.set(MAP_SIZE / 2, MAP_SIZE / 2, 2);
        get(UnitContainer.class).addUnit(createUnit());
        Item hoe = generator.generateItem("hoe", "iron", new Position(0, 0, 2));
        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(hoe, hoe.position);

        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(generator.generateItem("radish_seed", "generic_plant", null), new Position(1,0,2));
        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(generator.generateItem("radish_seed", "generic_plant", null), new Position(1,0,2));
        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(generator.generateItem("radish_seed", "generic_plant", null), new Position(1,0,2));
        get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(generator.generateItem("radish_seed", "generic_plant", null), new Position(1,0,2));

        SelectionTools.ZONE.type = ZoneTypeEnum.FARM;
        SelectionTools.ZONE.handleSelection(new Int3dBounds(4, 4, 2, 6, 6, 2));
        SelectionTools.ZONE.type = null;
    }

    private Unit createUnit() {
        Unit unit = new CreatureGenerator().generateUnit(new Position(3, 3, 2), "human");
        unit.get(JobsAspect.class).enabledJobs.add("farmer");
        return unit;
    }
}
