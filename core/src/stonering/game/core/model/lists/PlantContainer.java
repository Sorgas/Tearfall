package stonering.game.core.model.lists;

import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.TreeTileMapping;
import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.plants.Tree;

import java.util.ArrayList;

/**
 * Created by Alexander on 09.11.2017.
 * <p>
 * Contains plants and plants on localMap
 */
public class PlantContainer {
    private ArrayList<Tree> trees;
    private ArrayList<Plant> plants;
    private LocalMap localMap;
    private MaterialMap materialMap;

    public PlantContainer(ArrayList<Tree> trees, ArrayList<Plant> plants) {
        this.trees = trees;
        this.plants = plants;
        materialMap = MaterialMap.getInstance();
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public void placeTrees() {
        trees.forEach((tree) -> placeTree(tree));
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
                    Plant treePart = tree.getBlocks()[x][y][z];
                    if (treePart != null && localMap.getBlockType(mapX, mapY, mapZ) == 0) {
                        PlantBlock block = treePart.getBlock();
                        block.setAtlasX(TreeTileMapping.getType(block.getBlockType()).getAtlasX());
                        block.setAtlasY(materialMap.getAtlasY(block.getMaterial()));
                        localMap.setPlantBlock(mapX, mapY, mapZ, tree.getBlocks()[x][y][z].getBlock());
                    }
                }
            }
        }
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public void turn() {
    }
}
