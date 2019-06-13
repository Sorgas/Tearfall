package stonering.game.model.lists;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import stonering.entity.local.plants.*;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.IntervalTurnable;
import stonering.game.model.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.geometry.Position;
import stonering.entity.local.item.Item;
import stonering.util.global.CompatibleArray;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Contains plants. {@link Plant}s and {@link Tree}s are stored in list, {@link SubstratePlant} are stored in separate list.
 * One tile can have one plant block. Substrate plants can occupy same tile with other plants.
 * {@link PlantBlock}s are stored in map (by {@link Position}).
 * When plant is destroyed (died, cut or chopped), it's products are dropped via this container.
 * Plants do not move.
 * //TODO update passage map on blocks change.
 *
 * @author Alexander Kuzyakov on 09.11.2017.
 */
public class PlantContainer extends IntervalTurnable implements Initable, ModelComponent {
    private Array<AbstractPlant> plants;
    private Array<SubstratePlant> substratePlants;
    private HashMap<Position, PlantBlock> plantBlocks;
    private HashMap<Position, PlantBlock> substrateBlocks;
    private LocalMap localMap;
    private final int WALL_CODE = BlockTypesEnum.WALL.CODE;

    public PlantContainer() {
        this(Collections.EMPTY_LIST);
    }

    public PlantContainer(List<AbstractPlant> plants) {
        this.plants = new CompatibleArray<>(plants);
        substratePlants = new CompatibleArray<>();
        plantBlocks = new HashMap<>();
        substrateBlocks = new HashMap<>();
    }

    @Override
    public void init() {
        localMap = GameMvc.instance().getModel().get(LocalMap.class);
        plants.forEach(this::place);
    }

    @Override
    public void turn() {
        plants.forEach(abstractPlant -> abstractPlant.turn());
        substratePlants.forEach(SubstratePlant::turn);
    }

    /**
     * Entry method for placing plants and trees on map.
     */
    public void place(AbstractPlant plant) {
        if (plant.getType().isTree() && plant instanceof Tree) placeTree((Tree) plant);
        if (plant.getType().isPlant()) placePlant((Plant) plant);
        if (plant.getType().isSubstrate()) placeSubstrate((SubstratePlant) plant);
    }

    /**r
     * Places single-tile plant block into map to be rendered and accessed by other entities.
     * Block position should be set.
     */
    private void placePlant(Plant plant) {
        if(placeBlock(plant.getBlock())) plants.add(plant);
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
        List<Integer> treeForm = tree.getCurrentStage().treeForm;
        int radius = treeForm.get(0);
        Position vector = Position.sub(tree.getPosition(), radius, radius, treeForm.get(2)); // position of 0,0,0 tree part on map
        PlantBlock[][][] treeParts = tree.getBlocks();
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    if (treeParts[x][y][z] == null) continue;
                    Position onMapPosition = Position.add(vector, x, y, z);
                    if (!localMap.inMap(onMapPosition)) {
                        treeParts[x][y][z] = null; // remove block that is out of map
                        continue;
                    }
                    treeParts[x][y][z].setPosition(onMapPosition);
                    placeBlock(treeParts[x][y][z]);
                }
            }
        }
    }

    private void placeSubstrate(SubstratePlant plant) {
        if(substrateBlocks.containsKey(plant.getPosition())) return;
        substratePlants.add(plant);
        substrateBlocks.put(plant.getPosition(), plant.getBlock());
    }

    /**
     * Deletes plant from map and container
     */
    public void removePlant(Plant plant) {
        if (plants.removeValue(plant, true)) removeBlock(plant.getBlock());
    }

    /**
     * Removes tree blocks, can leave blocks products.
     */
    public void removeTree(Tree tree, boolean leaveProduct) {
        if (plants.removeValue(tree, true)) {
            int stompZ = tree.getCurrentStage().treeForm.get(2);
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = stompZ; z < treeParts[x][y].length; z++) {
                        PlantBlock block = treeParts[x][y][z];
                        if (block == null) continue;
                        removeBlock(block);
                        if(leaveProduct) leavePlantProduct(block);
                    }
                }
            }
        }
    }

    /**
     * Creates block's products in block's position.
     */
    private void leavePlantProduct(PlantBlock block) {
        ArrayList<Item> items = new PlantProductGenerator().generateCutProduct(block);
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        items.forEach((item) -> itemContainer.addItem(item));
    }

    /**
     * Method for all interactions that damages tree parts(chopping, braking branches, partial burning).
     * Handles different effects of this removing.
     * //TODO implementing out of mvp.
     */
    public void removeBlockFromTree(PlantBlock block, Tree tree) {
        fellTree(tree, OrientationEnum.N);
//        Position relPos = tree.getRelativePosition(block.getPosition());
//        tree.getBlocks()[relPos.getX()][relPos.getY()][relPos.getZ()] = null;
//        localMap.setPlantBlock(block.getPosition(), null);
        //TODO manage case for separating tree parts from each other
    }

    /**
     * Removes tree blocks and places blocks of fallen tree instead. On given orientation.
     * //TODO brake buildings or wooden constructions, damage creatures.
     */
    public void fellTree(Tree tree, OrientationEnum orientation) {
        if (orientation == OrientationEnum.N) {
            Position treePosition = tree.getPosition();
            int stompZ = tree.getCurrentStage().treeForm.get(2);
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = stompZ; z < treeParts[x][y].length; z++) {
                        PlantBlock block = treeParts[x][y][z];
                        if (block == null) continue;
                        Position newPosition = translatePosition(block.getPosition().toVector3(), treePosition.toVector3(), orientation);
                        if (localMap.getBlockType(newPosition) != WALL_CODE) {
                            removeBlock(block);
                            block.setPosition(newPosition);
                            placeBlock(block);
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
                        if (treeParts[x][y][z] != null)
                            removeBlock(treeParts[x][y][z]);
                    }
                }
            }
        } else if (plant instanceof Plant) {
            removeBlock(((Plant) plant).getBlock());
        }
    }

    /**
     * Puts block to blocks map by position from it.
     */
    private boolean placeBlock(PlantBlock block) {
        if(plantBlocks.containsKey(block.getPosition())) {
//            Logger.PLANTS.logDebug(block.getPlant() + " is blocked by " + plantBlocks.get(block.getPosition()).getPlant());
            return false; // tile is occupied
        }
        plantBlocks.put(block.getPosition(), block);
        return true;
    }

    /**
     * Removes block from blocks map and from it's plant.
     * If plant was single-tiled, it is destroyed
     */
    private void removeBlock(PlantBlock block) {
        if(plantBlocks.get(block.getPosition()) != block) {
            Logger.PLANTS.logError("Plant block with position " + block.getPosition() + " not stored in its position.") ;
        } else {
            plantBlocks.remove(block.getPosition());
        }
    }

    /**
     * Returns all plants with blocks in given position.
     */
    public AbstractPlant getPlantInPosition(Position position) {
        if(!plantBlocks.containsKey(position)) return null;
        return plantBlocks.get(position).getPlant();
    }

    public boolean isBlockPassable(Position position) {
        return !plantBlocks.containsKey(position) || plantBlocks.get(position).getType().passable;
    }

    public HashMap<Position, PlantBlock> getPlantBlocks() {
        return plantBlocks;
    }

    public HashMap<Position, PlantBlock> getSubstrateBlocks() {
        return substrateBlocks;
    }
}
