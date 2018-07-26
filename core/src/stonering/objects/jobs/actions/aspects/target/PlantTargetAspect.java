package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.plants.Plant;

public class PlantTargetAspect extends TargetAspect {
    private Plant plant;

    public PlantTargetAspect(Action action, Plant plant) {
        super(action);
        exactTarget = false;
        this.plant = plant;
    }

    @Override
    public Position getTargetPosition() {
        return plant.getPosition();
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
