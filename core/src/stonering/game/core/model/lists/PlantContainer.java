package stonering.game.core.model.lists;

import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.TreeTileMapping;
import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.plants.Tree;

import java.util.ArrayList;

/**
 * Contains plants on localMap. Trees are stored by their parts as separate plants.
 * Destroyed objects do not persist in container and their blocks are not in localMap.
 *
 * Created by Alexander on 09.11.2017.
 */
public class PlantContainer {
    private ArrayList<Plant> plants;
    private LocalMap localMap;

    public PlantContainer(ArrayList<Plant> plants) {
        this.plants = plants;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public void placePlants() {
        plants.forEach((plant) -> place(plant));
    }

    private void place(Plant plant) {
        if (plant.getType().isTree()) {
            placeTree(plant);
        } else {
            placePlant(plant);
        }
    }

    private void placePlant(Plant plant) {
        localMap.setPlantBlock(plant.getPosition(), plant.getBlock());
    }

    private void placeTree(Plant plant) {
//        int treeRadius = tree.getBlocks().length / 2;
//        int treeDepth = tree.getStompZ();
//        for (int x = 0; x < tree.getBlocks().length; x++) {
//            for (int y = 0; y < tree.getBlocks()[x].length; y++) {
//                for (int z = 0; z < tree.getBlocks()[x][y].length; z++) {
//                    int mapX = tree.getX() + x - treeRadius;
//                    int mapY = tree.getY() + y - treeRadius;
//                    int mapZ = tree.getZ() + z - treeDepth;
//                    Plant treePart = tree.getBlocks()[x][y][z];
//                    if (treePart != null && localMap.getBlockType(mapX, mapY, mapZ) == 0) {
//                        PlantBlock block = treePart.getBlock();
//                        block.setAtlasX(TreeTileMapping.getType(block.getBlockType()).getAtlasX());
//                        block.setAtlasY(MaterialMap.getInstance().getAtlasY(block.getMaterial()));
//                        localMap.setPlantBlock(mapX, mapY, mapZ, tree.getBlocks()[x][y][z].getBlock());
//                    }
//                }
//            }
//        }
    }

    /**
     * Deletes plant from map and container
     *
     * @param plant
     */
    public void removePlant(Plant plant) {
        if(plants.remove(plant)) {
            localMap.setPlantBlock(plant.getPosition(), null);
        }
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public void turn() {
    }
}
