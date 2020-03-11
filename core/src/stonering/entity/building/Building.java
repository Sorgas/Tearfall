package stonering.entity.building;

import stonering.entity.Entity;
import stonering.enums.OrientationEnum;
import stonering.enums.buildings.BuildingType;
import stonering.generators.buildings.BuildingGenerator;
import stonering.util.geometry.Position;

import java.util.function.Consumer;

/**
 * Represents furniture, workbenches and other built game entities.
 * [0,0] block points to SE corner of a building in any orientation.
 * All blocks are created in {@link BuildingGenerator};
 * Building position is position of its [0, 0] block.
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class Building extends Entity {
    public BuildingType type;
    public OrientationEnum orientation;
    public BuildingBlock[][] blocks;
    //TODO add parts with material.

    public boolean occupied = false;

    public Building(Position position, BuildingType type, OrientationEnum orientation) {
        super(position);
        this.type = type;
        this.orientation = orientation;
    }

    public void iterateBlocks(Consumer<BuildingBlock> consumer) {
        for (BuildingBlock[] row : blocks) {
            for (BuildingBlock block : row) {
                consumer.accept(block);
            }
        }
    }
    
    @Override
    public String toString() {
        return type.title;
    }
}
