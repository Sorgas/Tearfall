package stonering.entity.local.building;

import stonering.util.geometry.Position;

/**
 * As buildings can have multiple tiles, block represent single tile from a building.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingBlock {
    private Building building;
    private Position position;
    private String passage;

    public boolean isPassable() {
        return false;
    }

    public BuildingBlock(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPassage() {
        return passage;
    }

    public void setPassage(String passage) {
        this.passage = passage;
    }
}
