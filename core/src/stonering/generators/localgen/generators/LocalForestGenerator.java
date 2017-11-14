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
                int x = random.nextInt(localAreaSize - 10) + 5;
                int y = random.nextInt(localAreaSize - 10) + 5;
                placeTree(treesGenerator.generateTree("willow", 10), x, y);
            } catch (MaterialNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void placeTree(Tree tree, int treeX, int treeY) {
        int treeZ = container.getHeightsMap()[treeX][treeY];
        int[][][] treeBlocks = tree.getBlockTypes();
        int treeRadius = (treeBlocks.length - 1) / 2;
        int mapX = treeX - treeRadius;
        for (int x = 0; x < treeBlocks.length; x++) {
            int mapY = treeY - treeRadius;
            for (int y = 0; y < treeBlocks[0].length; y++) {
                int mapZ = treeZ - tree.getStompZ();
                for (int z = 0; z < treeBlocks[0][0].length; z++) {
                    if (treeBlocks[x][y][z] != 0 && inMap(mapX, mapY, mapZ)) {
                        localMap.setBlock(mapX, mapY, mapZ, BlockTypesEnum.WALL, tree.getWoodMaterial());
                        System.out.println("tree: " + mapX + " " + mapY + " " + mapZ);
                    }
                    mapZ++;
                }
                mapY++;
            }
            mapX++;
        }
    }

    private boolean inMap(int x, int y, int z) {
        return x < localAreaSize && y < localAreaSize && z < config.getAreaHight() && x >= 0 && y >= 0 && z >= 0;
    }
}
