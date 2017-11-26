package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.materials.TreeTypeMap;
import stonering.exceptions.MaterialNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.TreesGenerator;
import stonering.generators.worldgen.WorldMap;
import stonering.objects.plants.Tree;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alexander on 30.10.2017.
 */
public class LocalForestGenerator {
    private LocalGenContainer container;
    private MaterialMap materialMap;
    private WorldMap worldMap;
    private LocalGenConfig config;
    private int localAreaSize;
    private LocalMap localMap;
    private Random random;
    private TreeTypeMap treeTypeMap;
    private TreesGenerator treesGenerator;

    public LocalForestGenerator(LocalGenContainer container) {
        this.container = container;
        this.config = container.getConfig();
        this.localAreaSize = config.getAreaSize();
        this.localMap = container.getLocalMap();
        treesGenerator = new TreesGenerator(container);
    }

    public void execute() {
        random = new Random();
        for (int i = 0; i < 100; i++) {
            try {
                Tree tree = treesGenerator.generateTree("willow", 10);
                tree.setX(random.nextInt(localAreaSize - 10) + 5);
                tree.setY(random.nextInt(localAreaSize - 10) + 5);
                tree.setZ(container.getHeightsMap()[tree.getX()][tree.getY()]);
                container.getTrees().add(tree);
                placeTree(tree);
            } catch (MaterialNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void placeTree(Tree tree) {
        int treeRadius = tree.getBlockTypes().length / 2;
        int treeDepth = tree.getStompZ();
        for (int x = 0; x < tree.getBlockTypes().length; x++) {
            for (int y = 0; y < tree.getBlockTypes()[x].length; y++) {
                for (int z = 0; z < tree.getBlockTypes()[x][y].length; z++) {
                    int mapX = tree.getX() + x - treeRadius;
                    int mapY = tree.getY() + y - treeRadius;
                    int mapZ = tree.getZ() + z - treeDepth;
                    if (tree.getBlockTypes()[x][y][z] > 0 && localMap.getBlockType(mapX, mapY, mapZ) == 0) {
                        localMap.setBlock(mapX, mapY, mapZ, (byte) tree.getBlockTypes()[x][y][z], tree.getWoodMaterial());
                    }
                }
            }
        }
    }

    private boolean inMap(int x, int y, int z) {
        return x < localAreaSize && y < localAreaSize && z < config.getAreaHight() && x >= 0 && y >= 0 && z >= 0;
    }
}