package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.Tree;
import stonering.entity.unit.Unit;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing stages of single tree and chopping.
 *
 * @author Alexander_Kuzyakov
 */
public class SingleTreeModel extends TestModel {
    private static final int TREE_CENTER = MAP_SIZE / 2;

    @Override
    public void init() {
        super.init();
        get(UnitContainer.class).addUnit(createUnit());
        get(ItemContainer.class).onMapItemsSystem.putNewItem(createItem(), new Position(0, 0, 2));
        calendar.minute.max = 1;
        calendar.hour.max = 1;
        calendar.day.max = 4;
        get(PlantContainer.class).add(createTree(), new Position(TREE_CENTER, TREE_CENTER, 2));
    }

    private AbstractPlant createTree() {
        try {
            TreeGenerator treeGenerator = new TreeGenerator();
            Tree tree = treeGenerator.generateTree("willow", 0);
            return tree;
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Unit createUnit() {
        return new CreatureGenerator().generateUnit(new Position(2, 2, 2), "human");
    }

    private Item createItem() {
        return new ItemGenerator().generateItem("axe", "iron", null);
    }
}
