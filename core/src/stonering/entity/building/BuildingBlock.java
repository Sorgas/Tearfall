package stonering.entity.building;

import stonering.enums.blocks.PassageEnum;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.PassageEnum.PASSABLE;

/**
 * As buildings can have multiple tiles, block represent single tile from a building.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingBlock {
    public Building building; // building of this block
    public Position position; // block position on map
    public PassageEnum passage; // passage of this block

    public BuildingBlock(Building building, Position position, PassageEnum passage) {
        this.building = building;
        this.position = position;
        this.passage = passage;
    }
}
