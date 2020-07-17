package stonering.entity.plant.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.plants.PlantLifeStage;
import stonering.game.model.system.GameTime;
import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.Plant;
import stonering.entity.plant.Tree;
import stonering.enums.plants.PlantType;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * Stores plant age, and current stage index. see {@link stonering.game.model.system.plant.PlantGrowthSystem}
 * Stage length is measured in days.
 *
 * @author Alexander on 13.02.2019.
 */
public class PlantGrowthAspect extends Aspect {
    public int age; // in weeks
    public int currentStage;
    public boolean dead;
    public int counter;

    public PlantGrowthAspect(Entity entity) {
        this(entity, 0);
        currentStage = 0;
        dead = false;
        counter = 0;
    }

    public PlantGrowthAspect(Entity entity, int age) {
        super(entity);
        this.age = age;
    }

    public PlantLifeStage getCurrentStage() {
        if (dead) return null;
        List<PlantLifeStage> stages = ((AbstractPlant) entity).type.lifeStages;
        return stages.size() > currentStage ? stages.get(currentStage) : null;
    }
}
