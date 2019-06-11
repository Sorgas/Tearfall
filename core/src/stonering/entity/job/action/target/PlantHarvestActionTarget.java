package stonering.entity.job.action.target;

import stonering.util.geometry.Position;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;

/**
 * If target plant is tree, refreshes target position as some tree parts are harvested.
 *
 * @author Alexander on 25.09.2018.
 */
public class PlantHarvestActionTarget extends PlantActionTarget {

    public PlantHarvestActionTarget(AbstractPlant plant) {
        super(plant);
    }

    @Override
    public Position getPosition() {
        if (plant instanceof Plant) {
            return plant.getPosition();
        } else {
            return findUnharvestedPlantPart();
        }
    }

    private Position findUnharvestedPlantPart() {
        //TODO implement position fetching
        return plant.getPosition();
    }
}
