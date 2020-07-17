package stonering.entity.plant.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.plants.PlantLifeStage;
import stonering.entity.plant.AbstractPlant;

import java.util.List;

/**
 * Stores plant age, and current stage index. see {@link stonering.game.model.system.plant.PlantGrowthSystem}
 * Stage length is measured in days.
 *
 * @author Alexander on 13.02.2019.
 */
public class PlantGrowthAspect extends Aspect {
    public int age; // in days
    public int stageIndex;
    public int counter; // counts hours to next day
    public boolean dead;

    public PlantGrowthAspect(Entity entity) {
        this(entity, 0);
        stageIndex = 0;
        dead = false;
        counter = 0;
    }

    public PlantGrowthAspect(Entity entity, int age) {
        super(entity);
        this.age = age;
    }

    public PlantLifeStage getStage() {
        if (dead) return null;
        List<PlantLifeStage> stages = ((AbstractPlant) entity).type.lifeStages;
        return stages.size() > stageIndex ? stages.get(stageIndex) : null;
    }
}
