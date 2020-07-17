package stonering.entity.plant;

import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.plants.PlantType;
import stonering.game.model.system.plant.PlantContainer;
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

    public Tree(PlantType type) {
        super(type);
    }

    public PlantBlock[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(PlantBlock[][][] blocks) {
        this.blocks = blocks;
    }

    public PlantBlock getBlock(Position position) {
        Position target = Position.sub(position, getArrayStartPosition());
        return blocks[target.x][target.y][target.z];
    }

    /**
     * @return position of [0,0,0] block of tree on the map.
     */
    public Position getArrayStartPosition() {
        List<Integer> treeForm = get(PlantGrowthAspect.class).getStage().treeForm;
        int radius = treeForm.get(0);
        return Position.sub(position, radius, radius, treeForm.get(2));
    }
}
