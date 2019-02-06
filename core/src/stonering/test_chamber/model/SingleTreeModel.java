package stonering.test_chamber.model;

import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Tree;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.core.model.GameModel;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.model.lists.PlantContainer;
import stonering.generators.plants.TreesGenerator;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

public class SingleTreeModel extends GameModel {
    private static final int MAP_SIZE = 11;
    private static final int TREE_CENTER = 5;

    public SingleTreeModel() {
        reset();
    }

    /**
     * Recreates model.
     */
    public void reset() {
        put(createMap());
        put(new PlantContainer(createTree()));
    }

    private LocalMap createMap() {
        LocalMap localMap = new LocalMap(MAP_SIZE, MAP_SIZE, 20);
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, materialMap.getId("soil"));
                localMap.setBlock(x, y, 1, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
            }
        }
        return localMap;
    }

    private List<AbstractPlant> createTree() {
        List<AbstractPlant> plants = new ArrayList<>();
        try {
            TreesGenerator treesGenerator = new TreesGenerator();
            Tree tree = treesGenerator.generateTree("willow", 0);
            tree.setPosition(new Position(TREE_CENTER, TREE_CENTER, 1));
            plants.add(tree);
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
        return plants;
    }
}
