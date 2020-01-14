package stonering.entity.plant.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.system.plant.PlantSeedSystem;

/**
 * Stores configuration of plants seed item. Used by {@link PlantSeedSystem} to create seed items on map.
 *
 * @author Alexander on 14.01.2020.
 */
public class PlantSeedAspect extends Aspect {

    public PlantSeedAspect(Entity entity) {
        super(entity);
    }
}
