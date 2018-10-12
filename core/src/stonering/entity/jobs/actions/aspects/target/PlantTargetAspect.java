package stonering.entity.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;

public class PlantTargetAspect extends TargetAspect {
    protected AbstractPlant plant;

    public PlantTargetAspect(Action action, AbstractPlant plant) {
        super(action, true, true);
        this.plant = plant;
    }

    @Override
    public Position getTargetPosition() {
        return plant.getPosition();
    }

    public Plant getPlant() {
        return (Plant) plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
