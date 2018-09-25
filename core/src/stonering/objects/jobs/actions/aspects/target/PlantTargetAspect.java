package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.plants.AbstractPlant;
import stonering.objects.local_actors.plants.Plant;

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
