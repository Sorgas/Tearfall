package stonering.entity.local.building;

import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Proxy for BuildingType. This is stored on {@link LocalMap} and points to real object.
 *
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingBlock {
    private Building building;
    private Position position;
    private String passage;

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
