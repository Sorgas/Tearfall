package stonering.test_chamber.model;

import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.game.core.model.lists.PlantContainer;
import stonering.generators.plants.TreesGenerator;
import stonering.util.geometry.Position;

public class SingleTreeModel extends TestModel {
    private static final int MAP_SIZE = 11;
    private static final int TREE_CENTER = 5;

    private LocalMap localMap;
    private PlantContainer container;

    public SingleTreeModel() {
        reset();
    }

    private void createMap() {
        localMap = new LocalMap(MAP_SIZE, MAP_SIZE, 20);
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, materialMap.getId("soil"));
                localMap.setBlock(x, y, 1, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
            }
        }
    }

    private void createContainer() {
        container = new PlantContainer();
    }

    private void createTree() {
        try {
            TreesGenerator treesGenerator = new TreesGenerator();
            Tree tree = treesGenerator.generateTree("willow", 0);
            PlantBlock[][][] treeParts = tree.getBlocks();
            int treeCenterZ = tree.getType().getTreeType().getRootDepth();
            int treeRadius = tree.getType().getTreeType().getTreeRadius();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = 0; z < treeParts[x][y].length; z++) {
                        if (treeParts[x][y][z] != null) {
                            Position onMapPosition = new Position(
                                    TREE_CENTER + x - treeRadius,
                                    TREE_CENTER + y - treeRadius,
                                    TREE_CENTER + z - treeCenterZ);
                            localMap.setPlantBlock(onMapPosition, treeParts[x][y][z]);
                            treeParts[x][y][z].setPosition(onMapPosition);
                        }
                    }
                }
            }
            container.placeTree(tree);
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turn() {
        container.turn();
    }

    @Override
    public void reset() {
        createMap();
        createContainer();
        createTree();
    }
}
