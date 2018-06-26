package stonering.game.core.model.lists;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.plants.TreeType;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.generators.items.PlantProductGenerator;
import stonering.global.utils.Position;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.plants.AbstractPlant;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.plants.Tree;

import java.util.ArrayList;

/**
 * Contains plants on localMap. Trees are stored by their parts as separate plants.
 * Destroyed objects do not persist in container and their blocks are not in localMap.
 * <p>
 * @author Alexander Kuzyakov on 09.11.2017.
 */
public class PlantContainer {
    private ArrayList<AbstractPlant> plants;
    private GameContainer container;
    private LocalMap localMap;
    private final int WALL_CODE = BlockTypesEnum.WALL.getCode();


    public PlantContainer(GameContainer container) {
        this.container = container;
        localMap = container.getLocalMap();
        this.plants = new ArrayList<>();
    }

    public void placePlants(ArrayList<AbstractPlant> plants) {
        plants.forEach((plant) -> place(plant));
    }

    private void place(AbstractPlant plant) {
        if (plant.getType().isTree()) {
            if (plant instanceof Tree) {
                placeTree((Tree) plant);
            }
        } else {
            if (plant instanceof Plant) {
                placePlant((Plant) plant);
            }
        }
    }

    private void placePlant(Plant plant) {
        localMap.setPlantBlock(plant.getPosition(), plant.getBlock());
    }

    /**
     * Places tree on local map and adds it to container.
     * Tree should have position, pointing on its stomp (for growing from sapling).
     * //TODO checking space for placing
     * //TODO merging overlaps with other trees.
     *
     * @param tree Tree object with not null tree field
     */
    private void placeTree(Tree tree) {
        TreeType treeType = tree.getType().getTreeType();
        PlantBlock[][][] treeParts = tree.getBlocks();
        Position vector = new Position(tree.getPosition().getX() - treeType.getTreeRadius(),
                tree.getPosition().getY() - treeType.getTreeRadius(),
                tree.getPosition().getZ() - treeType.getRootDepth()); // position of 0,0,0 tree part on map
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    if (treeParts[x][y][z] != null) {
                        Position onMapPosition = new Position(vector.getX() + x,
                                vector.getY() + y,
                                vector.getZ() + z);
                        localMap.setPlantBlock(onMapPosition, treeParts[x][y][z]);
                        treeParts[x][y][z].setPosition(onMapPosition);
                    }
                }
            }
        }
        plants.add(tree);
    }

    /**
     * Deletes plant from map and container
     *
     * @param plant
     */
    public void removePlant(Plant plant) {
        if (plants.remove(plant)) {
            localMap.setPlantBlock(plant.getPosition(), null);
        }
    }

    public void removeTree(Tree tree) {
        if (plants.remove(tree)) {
            int stompZ = tree.getType().getTreeType().getRootDepth();
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = stompZ; z < treeParts[x][y].length; z++) {
                        PlantBlock block = treeParts[x][y][z];
                        if (block != null) {
                            localMap.setPlantBlock(block.getPosition(), null);
                            leavePlantProduct(block);
                        }
                    }
                }
            }
        }
    }

    private void leavePlantProduct(PlantBlock block) {
        ArrayList<Item> items = new PlantProductGenerator().generateCutProduct(block);
        items.forEach((item) -> container.getItemContainer().addItem(item, block.getPosition()));
    }

    /**
     * Deletes block from map and it's plant. If plants was Plant, deletes is too.
     * If plant was Tree than checks deleting for other effects.
     *
     * @param block
     */
    public void removePlantBlock(PlantBlock block, boolean leaveProducts) {
        AbstractPlant plant = block.getPlant();
        if (plant != null) {
            if (plant instanceof Plant) {
                if (plants.remove(plant)) {
                    localMap.setPlantBlock(block.getPosition(), null);
                }
            } else if (plant instanceof Tree) {
                removeBlockFromTree(block, (Tree) plant);
            }
        }
    }

    public void removeBlockFromTree(PlantBlock block, Tree tree) {
        fellTree(tree, OrientationEnum.N);
//        Position relPos = tree.getRelativePosition(block.getPosition());
//        tree.getBlocks()[relPos.getX()][relPos.getY()][relPos.getZ()] = null;
//        localMap.setPlantBlock(block.getPosition(), null);
        //TODO manage case for separating tree parts from each other
    }

    public void fellTree(Tree tree, OrientationEnum orientation) {
        if (orientation == OrientationEnum.N) {
            Position treePosition = tree.getPosition();
            int stompZ = tree.getType().getTreeType().getRootDepth();
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = stompZ; z < treeParts[x][y].length; z++) {
                        PlantBlock block = treeParts[x][y][z];
                        if (block != null) {
                            Position newPosition = translatePosition(block.getPosition().toVector3(), treePosition.toVector3(), orientation);
                            if (container.getLocalMap().getBlockType(newPosition) != WALL_CODE) {
                                localMap.setPlantBlock(block.getPosition(), null);
                                block.setPosition(newPosition);
                                localMap.setPlantBlock(block.getPosition(), block);
                            } else {
                                treeParts[x][y][z] = null;
                            }
                        }
                    }
                }
            }
        }
    }

    private Position translatePosition(Vector3 position, Vector3 center, OrientationEnum orientation) {
        Vector3 offset = position.sub(center);
        Matrix3 matrix3 = new Matrix3();
        matrix3.setToRotation(new Vector3(1, 0, 0), -90);
        offset.mul(matrix3);
        return new Position(center.add(offset.mul(matrix3)));
    }

    private void leaveproducts(ArrayList<String> products) {

    }

    private void checkTree(Tree tree, Position deletedPart) {

    }

    public void turn() {
    }

    public ArrayList<AbstractPlant> getPlants() {
        return plants;
    }
}
