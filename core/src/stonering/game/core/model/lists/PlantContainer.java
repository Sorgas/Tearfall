package stonering.game.core.model.lists;

import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.Tree;

import java.util.ArrayList;

/**
 * Created by Alexander on 09.11.2017.
 *
 * Contains plants and trees on localMap
 */
public class PlantContainer {
    private ArrayList<Tree> trees;
    private ArrayList<Plant> plants;
    private LocalMap localMap;

    public PlantContainer(ArrayList<Tree> trees, ArrayList<Plant> plants) {
        this.trees = trees;
        this.plants = plants;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
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

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public void placeTrees() {
        trees.forEach((tree) -> placeTree(tree));
    }
}
