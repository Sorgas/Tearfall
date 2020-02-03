package stonering.entity.building;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.util.geometry.Position;

/**
 * As buildings can have multiple tiles, block represent single tile from a building.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingBlock {
    public Building building;
    public Position position;
    public String passage;

    public boolean isPassable() {
        return BlockTypeEnum.getType(passage).PASSING == BlockTypeEnum.PassageEnum.PASSABLE;
    }

    public BuildingBlock(Building building) {
        this.building = building;
    }
}
