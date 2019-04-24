package stonering.entity.local.plants;

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

    public Tree(Position position, int age) {
        super(position);
        this.age = age;
    }

    public Position getRelativePosition(Position mapPos) {
        return new Position(
                mapPos.x + getCurrentStage().getTreeRadius() - position.x,
                mapPos.y + getCurrentStage().getTreeRadius() - position.y,
                mapPos.z + getCurrentStage().treeForm.get(2) - position.z
        );
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
        return getCurrentStage().harvestProducts != null;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        updateBlockPositions();
    }
}
