package stonering.game.model.system.plant;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import stonering.entity.plant.*;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntityContainer;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.system.item.ItemContainer;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.geometry.Position;
import stonering.entity.item.Item;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains plants. {@link Plant}s and {@link Tree}s are stored in list. One tile can have one plant block.
 * {@link PlantBlock}s are stored in map (by {@link Position}).
 * When plant is destroyed (died, cut or chopped), it's products are dropped via this container.
 * Plants are updated once in a minute. Plants do not move.
 * //TODO update passage map on blocks change.
 * TODO add deletion list
 *
 * @author Alexander Kuzyakov on 09.11.2017.
 */
public class PlantContainer extends EntityContainer<AbstractPlant> implements Initable, ModelComponent {
    private HashMap<Position, PlantBlock> plantBlocks; // trees and plants blocks

    private LocalMap localMap;
    private final int WALL_CODE = BlockTypeEnum.WALL.CODE;

    public PlantContainer() {
        plantBlocks = new HashMap<>();
        put(new PlantSeedSystem());
        put(new PlantGrowthSystem());
    }

    @Override
    public void init() {
        localMap = GameMvc.instance().model().get(LocalMap.class);
    }

    /**
     * Entry method for placing new plants and trees on map.
     */
    public void place(AbstractPlant plant, Position position) {
        if (plant.type.isTree) placeTree((Tree) plant, position);
        if (plant.type.isPlant) placePlant((Plant) plant, position);
    }

    /**
     * Places single-tile plant block into map to be rendered and accessed by other entities.
     * Block position should be set.
     */
    private void placePlant(Plant plant, Position position) {
        plant.setPosition(position);
        if (placeBlock(plant.getBlock())) objects.add(plant);
    }

    /**
     * Places tree on local map.
     * Tree should have position, pointing on its stomp (for growing from sapling).
     * //TODO checking space for placing
     * //TODO merging overlaps with other trees.
     *
     * @param tree Tree object with not null tree field
     */
    private void placeTree(Tree tree, Position position) {
        objects.add(tree);
        tree.setPosition(position);
        Position vector = tree.getArrayStartPosition();
        PlantBlock[][][] treeParts = tree.getBlocks();
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    if (treeParts[x][y][z] == null) continue;
                    Position onMapPosition = Position.add(vector, x, y, z);
                    if (!localMap.inMap(onMapPosition)) {
                        treeParts[x][y][z] = null; // remove block that is out of map
                    } else {
                        treeParts[x][y][z].setPosition(onMapPosition);
                        placeBlock(treeParts[x][y][z]);
                    }
                }
            }
        }
    }


    /**
     * Puts block to blocks map by position from it.
     */
    private boolean placeBlock(PlantBlock block) {
        if (plantBlocks.containsKey(block.getPosition())) {
            Logger.PLANTS.logDebug(block.getPlant() + " is blocked by " + plantBlocks.get(block.getPosition()).getPlant());
            return false; // tile is occupied
        }
        Position blockPosition = block.getPosition();
        plantBlocks.put(block.getPosition(), block);
        localMap.updatePassage(blockPosition.x, blockPosition.y, blockPosition.z);
        return true;
    }

    /**
     * Removes plant from map completely. Can leave products.
     */
    public void remove(AbstractPlant plant, boolean leaveProduct) {
        if (plant != null && objects.remove(plant)) removePlantBlocks(plant, leaveProduct);
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
                        if (treeParts[x][y][z] != null) removeBlock(treeParts[x][y][z], leaveProduct);
                    }
                }
            }
        } else if (plant instanceof Plant) {
            removeBlock(((Plant) plant).getBlock(), leaveProduct);
        }
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
            Position blockPosition = block.getPosition();
            plantBlocks.remove(blockPosition);
            if (leaveProduct) leavePlantProduct(block);
            localMap.updatePassage(blockPosition.x, blockPosition.y, blockPosition.z);
        }
    }

    /**
     * Creates block's products in block's position.
     */
    private void leavePlantProduct(PlantBlock block) {
        ArrayList<Item> items = new PlantProductGenerator().generateCutProduct(block);
        ItemContainer itemContainer = GameMvc.instance().model().get(ItemContainer.class);
        for (Item item : items) {
            itemContainer.addItem(item);
            itemContainer.onMapItemsSystem.putItem(item, block.getPosition());
        }
    }

    /**
     * Method for all interactions that damages tree parts(chopping, braking branches, partial burning).
     * Handles different effects of this removing.
     * //TODO implementing out of mvp.
     */
    public void removeBlockFromTree(PlantBlock block, Tree tree) {
//        fellTree(tree, OrientationEnum.N);
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
            Position treePosition = tree.position;
            PlantType type = tree.type;
            int stompZ = type.lifeStages.get(tree.getAspect(PlantGrowthAspect.class).currentStage).treeForm.get(2);
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

    public boolean isPlantBlockPassable(Position position) {
        return !plantBlocks.containsKey(position) || plantBlocks.get(position).isPassable();
    }

    public AbstractPlant getPlantInPosition(Position position) {
        return plantBlocks.containsKey(position) ? plantBlocks.get(position).getPlant() : null;
    }

    public PlantBlock getPlantBlock(Position position) {
        return plantBlocks.get(position);
    }

    public boolean isPlantBlockExists(Position position) {
        return plantBlocks.containsKey(position);
    }

    public void handleBlockRemoval(Position position) {
        if(!plantBlocks.containsKey(position)) return;
        PlantBlock block = plantBlocks.get(position);
        if(block.getPlant().type.isTree) {
            //TODO trees should have underground roots, and stay while enough root blocks are in the soil
        } else if(block.getPlant().type.isPlant) {
            remove(block.getPlant(), true);
        }
    }
}
