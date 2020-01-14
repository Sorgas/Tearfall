package stonering.entity.plant;

import stonering.enums.plants.PlantType;
import stonering.game.model.system.PlantContainer;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * Multi-tile plant, generally for trees. Position is coordinates of tree stomp on map.
 * Blocks store absolute position on map. For storing in {@link PlantContainer}.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class Tree extends AbstractPlant {
    private PlantBlock[][][] blocks; //TODO replace with map(vector from stomp, block)

    public Tree(PlantType type, int age) {
        super(type, age);
    }

    public PlantBlock[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(PlantBlock[][][] blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean isHarvestable() {
        return getCurrentStage().harvestProduct != null;
    }

    public PlantBlock getBlock(Position position) {
        Position target = Position.sub(position, getArrayStartPosition());
        return blocks[target.x][target.y][target.z];
    }

    /**
     * @return position of [0,0,0] block of tree on the map.
     */
    public Position getArrayStartPosition() {
        List<Integer> treeForm = getCurrentStage().treeForm;
        int radius = treeForm.get(0);
        return Position.sub(position, radius, radius, treeForm.get(2));
    }
}
