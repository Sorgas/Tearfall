package stonering.game.model.lists;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import stonering.entity.local.Entity;
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
    private Array<AbstractPlant> plants; // trees and plants
    private Array<SubstratePlant> substratePlants;
    private HashMap<Position, PlantBlock> plantBlocks; // trees and plants blocks
    private HashMap<Position, PlantBlock> substrateBlocks;

    private LocalMap localMap;
    private final int WALL_CODE = BlockTypesEnum.WALL.CODE;

    public PlantContainer() {
        this(new ArrayList<>());
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
        plants.forEach(Entity::turn);
        substratePlants.forEach(SubstratePlant::turn);
    }

    /**
     * Entry method for placing plants and trees on map.
     */
    public void place(AbstractPlant plant) {
        if (plant.getType().isTree()) placeTree((Tree) plant);
        if (plant.getType().isPlant()) placePlant((Plant) plant);
        if (plant.getType().isSubstrate()) placeSubstrate((SubstratePlant) plant);
    }

    /**
     * r
     * Places single-tile plant block into map to be rendered and accessed by other entities.
     * Block position should be set.
     */
    private void placePlant(Plant plant) {
        if (placeBlock(plant.getBlock())) plants.add(plant);
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
        Position vector = tree.getArrayStartPosition();
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
        if (substrateBlocks.containsKey(plant.getPosition())) return;
        substratePlants.add(plant);
        substrateBlocks.put(plant.getPosition(), plant.getBlock());
    }

    /**
     * Puts block to blocks map by position from it.
     */
    private boolean placeBlock(PlantBlock block) {
        if (plantBlocks.containsKey(block.getPosition())) {
            Logger.PLANTS.logDebug(block.getPlant() + " is blocked by " + plantBlocks.get(block.getPosition()).getPlant());
            return false; // tile is occupied
        }
        plantBlocks.put(block.getPosition(), block);
        return true;
    }

    /**
     * Removes plant from map completely. Can leave products.
     */
    public void remove(AbstractPlant plant, boolean leaveProduct) {
        if (plant.getType().isSubstrate()) {
            removeSubstrate((SubstratePlant) plant);
        } else {
            removePlant(plant, leaveProduct);
        }
    }

    /**
     * Deletes plant or tree from map and container. Can leave products.
     */
    public void removePlant(AbstractPlant plant, boolean leaveProduct) {
        if (plants.removeValue(plant, true)) removePlantBlocks(plant, leaveProduct);
    }

    /**
     * Removes substrate plant and it's block.
     */
    public void removeSubstrate(SubstratePlant plant) {
        if (substrateBlocks.containsKey(plant.getPosition())) return;
        substratePlants.removeValue(plant, true);
        substrateBlocks.remove(plant.getPosition());
    }

    /**
     * Removes blocks of given plant or tree from map. Can leave products.
     */
    public void removePlantBlocks(AbstractPlant plant, boolean leaveProduct) {
        if (plant instanceof Tree) {
            Tree tree = (Tree) plant;
            PlantBlock[][][] treeParts = tree.getBlocks();
            for (int x = 0; x < treeParts.length; x++) {
                for (int y = 0; y < treeParts[x].length; y++) {
                    for (int z = 0; z < treeParts[x][y].length; z++) {
                        if (treeParts[x][y][z] != null)
                            plantBlocks.remove(treeParts[x][y][z]);
                        if (leaveProduct) leavePlantProduct(treeParts[x][y][z]);
                    }
                }
            }
        } else if (plant instanceof Plant) {
            plantBlocks.remove(((Plant) plant).getBlock().getPosition());
            if (leaveProduct) leavePlantProduct(((Plant) plant).getBlock());
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
     * Removes block from blocks map and from it's plant.
     * If plant was single-tiled, it is destroyed.
     */
    private void removeBlock(PlantBlock block, boolean leaveProduct) {
        if (plantBlocks.get(block.getPosition()) != block) {
            //TODO debug
            Logger.PLANTS.logError("Plant block with position " + block.getPosition() + " not stored in its position.");
        } else {
            plantBlocks.remove(block.getPosition());
        }
    }

    /**
     * Removes plants and tree parts from position.
     *
     * @param position
     */
    public void removePlant(Position position) {
        PlantBlock block = plantBlocks.get(position);

        if (plantBlocks.containsKey(position)) removeBlock(plantBlocks.get(position));
    }

    /**
     * Removes substrate plant from given position if there is any.
     */
    public void removeSubstrate(Position position) {
        if (!substrateBlocks.containsKey(position)) return;
        substratePlants.removeValue((SubstratePlant) substrateBlocks.remove(position).getPlant(), true);
    }

    /**
     * Returns all plants with blocks in given position.
     */
    public AbstractPlant getPlantInPosition(Position position) {
        if (!plantBlocks.containsKey(position)) return null;
        return plantBlocks.get(position).getPlant();
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
                            plantBlocks.remove(block.getPosition());
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

    /**
     * Translates block position of growing tree to position of fallen one.
     */
    private Position translatePosition(Vector3 position, Vector3 center, OrientationEnum orientation) {
        Vector3 offset = position.sub(center);
        Matrix3 matrix3 = new Matrix3();
        matrix3.setToRotation(new Vector3(1, 0, 0), -90);
        offset.mul(matrix3);
        return new Position(center.add(offset.mul(matrix3)));
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
