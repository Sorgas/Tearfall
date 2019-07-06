package stonering.test_chamber.model;

import stonering.entity.environment.GameCalendar;
import stonering.entity.plants.AbstractPlant;
import stonering.entity.plants.Tree;
import stonering.entity.World;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing stages of single tree.
 *
 * @author Alexander_Kuzyakov
 */
public class SingleTreeModel extends TestModel {
    private static final int TREE_CENTER = MAP_SIZE / 2;

    @Override
    public void init() {
        super.init();
        get(GameCalendar.class).addListener("minute", get(World.class).getStarSystem());
        get(GameCalendar.class).addListener("minute", get(PlantContainer.class));
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
}
