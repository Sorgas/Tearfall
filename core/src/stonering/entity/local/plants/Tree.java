package stonering.entity.local.plants;

import stonering.enums.plants.PlantType;
import stonering.game.model.lists.PlantContainer;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * Multi-tile plant, generally for trees. Position is coordinates of tree stomp on map.
 * Blocks store absolute position on map. For storing in {@link PlantContainer}
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class Tree extends AbstractPlant {
    private PlantBlock[][][] blocks; //TODO replace with map(vector from stomp, block)

    public Tree(Position position, PlantType type, int age) {
        super(position, type, age);
    }

    /**
     * Changes positions in blocks according to main plant position.
     */
    private void updateBlockPositions() {

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

    public void setPosition(Position position) {
        super.setPosition(position);
        updateBlockPositions();
    }
}
