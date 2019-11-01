package stonering.test_chamber.model;

import stonering.entity.item.Item;
import stonering.entity.plants.AbstractPlant;
import stonering.entity.plants.Tree;
import stonering.entity.unit.Unit;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.PlantContainer;
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
        get(ItemContainer.class).addAndPut(createItem());
        calendar.minute.setSize(1);
        calendar.hour.setSize(1);
        calendar.day.setSize(4);
        get(PlantContainer.class).place(createTree(), new Position(TREE_CENTER, TREE_CENTER, 2));
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
        Item item = new ItemGenerator().generateItem("axe", "iron", new Position(0, 0, 2));
        return item;
    }
}
