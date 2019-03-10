package stonering.game.core.model.lists;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.plants.TreeType;
import stonering.game.core.GameMvc;
import stonering.game.core.model.IntervalTurnable;
import stonering.game.core.model.ModelComponent;
import stonering.game.core.model.local_map.LocalMap;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.geometry.Position;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;
import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains plants on localMap. Trees are stored by their parts as separate plants.
 * Destroyed entity do not persist in container and their blocks are not in localMap.
 * <p>
 *
 * @author Alexander Kuzyakov on 09.11.2017.
 */
public class PlantContainer extends IntervalTurnable implements Initable, ModelComponent {
    private GameMvc gameMvc;
    private List<AbstractPlant> plants;
    private LocalMap localMap;
    private final int WALL_CODE = BlockTypesEnum.WALL.CODE;

    public PlantContainer(List<AbstractPlant> plants) {
        this.plants = plants;
    }

    @Override
    public void init() {
        gameMvc = GameMvc.getInstance();
        localMap = gameMvc.getModel().get(LocalMap.class);
        plants.forEach(this::place);
    }

    @Override
    public void turn() {
        plants.forEach(abstractPlant -> abstractPlant.turn());
    }

    public void place(AbstractPlant plant) {
        if (plant.getType().isTree() && plant instanceof Tree) placeTree((Tree) plant);
        if (plant instanceof Plant) placePlant((Plant) plant);
    }

    private void placePlant(Plant plant) {
        localMap.setPlantBlock(plant.getPosition(), plant.getBlock());
        plant.getBlock().setPosition(plant.getPosition());
    }

    /**
     * Places tree on local map.
     * Tree should have position, pointing on its stomp (for growing from sapling).
     * //TODO checking space for placing
     * //TODO merging overlaps with other trees.
     *
     * @param tree Tree object with not null tree field
     */
    private void placeTree(Tree tree) {
        TreeType type = tree.getCurrentStage().getTreeType();
        int radius = type.getTreeRadius();
        Position vector = Position.sub(tree.getPosition(), radius, radius, type.getRootDepth()); // position of 0,0,0 tree part on map
        PlantBlock[][][] treeParts = tree.getBlocks();
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    if (treeParts[x][y][z] == null) continue;
                    Position onMapPosition = Position.add(vector, x, y, z);
                    if (!localMap.inMap(onMapPosition)) continue;
                    localMap.setPlantBlock(onMapPosition, treeParts[x][y][z]);
                    treeParts[x][y][z].setPosition(onMapPosition);
                }
            }
        }
    }

    /**
     * Deletes plant from map and container
     *
     * @param plant
     */
    public void removePlant(Plant plant) {
        if (plants.remove(plant)) localMap.setPlantBlock(plant.getPosition(), null);
    }

    public void removeTree(Tree tree) {
        if (plants.remove(tree)) {
            int stompZ = tree.getCurrentStage().getTreeType().getRootDepth();
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = stompZ; z < treeParts[x][y].length; z++) {
                        PlantBlock block = treeParts[x][y][z];
                        if (block == null) continue;
                        localMap.setPlantBlock(block.getPosition(), null);
                        leavePlantProduct(block);
                    }
                }
            }
        }
    }

    private void leavePlantProduct(PlantBlock block) {
        ArrayList<Item> items = new PlantProductGenerator().generateCutProduct(block);
        items.forEach((item) -> gameMvc.getModel().get(ItemContainer.class).addItem(item, block.getPosition()));
    }

    /**
     * Deletes block from map and it's plant. If plants was a Plant, deletes is too.
     * If plant was a Tree than checks deleting for other effects.
     *
     * @param block
     */
    public void removePlantBlock(PlantBlock block, boolean leaveProducts) {
        AbstractPlant plant = block.getPlant();
        if (plant == null) return;
        if (plant instanceof Plant) {
            if (plants.remove(plant)) localMap.setPlantBlock(block.getPosition(), null);
        } else if (plant instanceof Tree) {
            removeBlockFromTree(block, (Tree) plant);
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
            int stompZ = tree.getCurrentStage().getTreeType().getRootDepth();
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = stompZ; z < treeParts[x][y].length; z++) {
                        PlantBlock block = treeParts[x][y][z];
                        if (block == null) continue;
                        Position newPosition = translatePosition(block.getPosition().toVector3(), treePosition.toVector3(), orientation);
                        if (localMap.getBlockType(newPosition) != WALL_CODE) {
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

    private Position translatePosition(Vector3 position, Vector3 center, OrientationEnum orientation) {
        Vector3 offset = position.sub(center);
        Matrix3 matrix3 = new Matrix3();
        matrix3.setToRotation(new Vector3(1, 0, 0), -90);
        offset.mul(matrix3);
        return new Position(center.add(offset.mul(matrix3)));
    }

    /**
     * Removes blocks of given plant from map.
     */
    public void removePlantBlocks(AbstractPlant plant) {
        if (plant instanceof Tree) {
            Tree tree = (Tree) plant;
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = 0; z < treeParts[x][y].length; z++) {
                        if (treeParts[x][y][z] != null) localMap.setPlantBlock(treeParts[x][y][z].getPosition(), null);
                    }
                }
            }
        } else if (plant instanceof Plant) {
            localMap.setPlantBlock(((Plant) plant).getBlock().getPosition(), null);
        }
    }

    public List<AbstractPlant> getPlants() {
        return plants;
    }
}
