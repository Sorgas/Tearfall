package stonering.game.model.system.plant;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.entity.plant.aspects.PlantSeedAspect;
import stonering.game.model.system.EntitySystem;

/**
 * System that checks plants for and spreads seeds around.
 * Small plants just create plants nearby, Trees and bushes drop items(like apples and nuts) that can grow to new plant.
 *
 * @author Alexander on 14.01.2020.
 */
public class PlantSeedSystem extends EntitySystem<AbstractPlant> {

    public PlantSeedSystem() {
        targetAspects.add(PlantSeedAspect.class); // contains seed details
        targetAspects.add(PlantGrowthAspect.class); // contains age information
    }

    @Override
    public void update(AbstractPlant entity) {

    }
}
