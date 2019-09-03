package stonering.entity.job.action.target;

import stonering.util.geometry.Position;
import stonering.entity.plants.AbstractPlant;

public class PlantActionTarget extends ActionTarget {
    protected AbstractPlant plant;

    public PlantActionTarget(AbstractPlant plant) {
        super(ActionTarget.ANY);
        this.plant = plant;
    }

    @Override
    public Position getPosition() {
        return plant.position;
    }

    public AbstractPlant getPlant() {
        return plant;
    }

    public void setPlant(AbstractPlant plant) {
        this.plant = plant;
    }
}
