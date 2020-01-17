package stonering.game.model.system.plant;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.entity.plant.aspects.PlantProductAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;

/**
 * System that creates products on plants (like berries), and make them fall down (like apples).
 *
 * @author Alexander on 17.01.2020.
 */
public class PlantProductSystem extends EntitySystem<AbstractPlant> {

    public PlantProductSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
        targetAspects.add(PlantGrowthAspect.class);
        targetAspects.add(PlantProductAspect.class);
    }

    @Override
    public void update(AbstractPlant entity) {

    }
}
