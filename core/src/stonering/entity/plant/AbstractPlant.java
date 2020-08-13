package stonering.entity.plant;

import stonering.entity.Entity;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.plants.PlantLifeStage;
import stonering.enums.plants.PlantType;
import stonering.util.geometry.Position;

/**
 * Parent class for single and multi tile plants.
 */
public abstract class AbstractPlant extends Entity {
    public final PlantType type;

    protected AbstractPlant(Position position, PlantType type) {
        super(position);
        this.type = type;
    }

    protected AbstractPlant(PlantType type) {
        super();
        this.type = type;
    }

    public PlantLifeStage getCurrentLifeStage() {
        return optional(PlantGrowthAspect.class).map(aspect -> type.lifeStages.get(aspect.stageIndex)).orElse(null);
    }
}
