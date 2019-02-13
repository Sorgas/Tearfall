package stonering.entity.local.items.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;

/**
 * Items with this aspect can create plants.
 *
 * @author Alexander on 13.02.2019.
 */
public class PlantSeedAspect extends Aspect {
    private static final String NAME = "plant_seed";
    private String plantName;

    public PlantSeedAspect(AspectHolder aspectHolder, String plantName) {
        super(aspectHolder);
        this.plantName = plantName;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
