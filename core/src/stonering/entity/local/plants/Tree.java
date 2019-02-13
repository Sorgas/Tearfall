package stonering.entity.local.plants;

import stonering.util.geometry.Position;

/**
 * Multitile plant, generally for trees. Position is coordinates of tree stomp on map.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class Tree extends AbstractPlant {
    private Position position;           //position of stomp
    private PlantBlock[][][] blocks;

    public Tree(Position position, int age) {
        super(position);
        this.age = age;
    }

    public Position getRelativePosition(Position mapPos) {
        return new Position(
                mapPos.x + type.getTreeType().getTreeRadius() - position.x,
                mapPos.y + type.getTreeType().getTreeRadius() - position.y,
                mapPos.z + type.getTreeType().getRootDepth() - position.z
        );
    }

    public PlantBlock[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(PlantBlock[][][] blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean isHarvestable() {
        return getCurrentStage().getHarvestProducts() != null;
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
    }
}
