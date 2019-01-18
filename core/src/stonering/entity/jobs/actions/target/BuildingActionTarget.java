package stonering.entity.jobs.actions.target;

import stonering.entity.local.building.Building;
import stonering.util.geometry.Position;

/**
 * Targets a building. Is valid while building persists.
 */
public class BuildingActionTarget extends ActionTarget {
    //TODO add work position offset for buildings, so unit can work only from one side of a building.
    private Building building;

    public BuildingActionTarget(boolean exactTarget, boolean nearTarget, Building building) {
        super(exactTarget, nearTarget);
        this.building = building;
    }

    @Override
    public Position getPosition() {
        return building.getPosition();
    }

    public Building getBuilding() {
        return building;
    }
}
