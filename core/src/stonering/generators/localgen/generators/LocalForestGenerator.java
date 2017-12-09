package stonering.generators.localgen.generators;

import stonering.exceptions.MaterialNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.TreesGenerator;
import stonering.objects.local_actors.plants.Tree;

import java.util.Random;

/**
 * Created by Alexander on 30.10.2017.
 */
public class LocalForestGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private int localAreaSize;
    private LocalMap localMap;
    private Random random;
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
        int treeRadius = tree.getBlocks().length / 2;
        int treeDepth = tree.getStompZ();
        for (int x = 0; x < tree.getBlocks().length; x++) {
            for (int y = 0; y < tree.getBlocks()[x].length; y++) {
                for (int z = 0; z < tree.getBlocks()[x][y].length; z++) {
                    int mapX = tree.getX() + x - treeRadius;
                    int mapY = tree.getY() + y - treeRadius;
                    int mapZ = tree.getZ() + z - treeDepth;
                    if (tree.getBlocks()[x][y][z] != null && localMap.getBlockType(mapX, mapY, mapZ) == 0) {
                        localMap.setPlantBlock(mapX, mapY, mapZ, tree.getBlocks()[x][y][z]);
                    }
                }
            }
        }
    }

    private boolean inMap(int x, int y, int z) {
        return x < localAreaSize && y < localAreaSize && z < config.getAreaHight() && x >= 0 && y >= 0 && z >= 0;
    }
}