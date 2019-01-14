package stonering.entity.jobs.actions.aspects.target;

import stonering.util.geometry.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;

public class PlantActionTarget extends ActionTarget {
    protected AbstractPlant plant;

    public PlantActionTarget(Action action, AbstractPlant plant) {
        super(action, true, true);
        this.plant = plant;
    }

    @Override
    public Position getPosition() {
        return plant.getPosition();
    }

    public Plant getPlant() {
        return (Plant) plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
