package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.plants.AbstractPlant;
import stonering.objects.local_actors.plants.Plant;

/**
 * If target plant is tree, refreshes target position as some tree parts are harvested.
 *
 * @author Alexander on 25.09.2018.
 */
public class PlantHarvestTargetAspect extends PlantTargetAspect{

    public PlantHarvestTargetAspect(Action action, AbstractPlant plant) {
        super(action, plant);
    }

    @Override
    public Position getTargetPosition() {
        if(plant instanceof Plant) {
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
