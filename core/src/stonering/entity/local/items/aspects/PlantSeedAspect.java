package stonering.entity.local.items.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;

/**
 * Items with this aspect can create plants.
 *
 * @author Alexander on 13.02.2019.
 */
public class PlantSeedAspect extends Aspect {
    public static final String NAME = "plant_seed";
    private String plantName;

    public PlantSeedAspect(Entity entity, String plantName) {
        super(entity);
        this.plantName = plantName;
    }
}
