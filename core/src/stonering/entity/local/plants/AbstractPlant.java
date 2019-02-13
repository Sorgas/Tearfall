package stonering.entity.local.plants;

import stonering.entity.local.AspectHolder;
import stonering.enums.plants.PlantType;
import stonering.util.geometry.Position;

/**
 * Parent class for single and multi tile plants.
 */
public abstract class AbstractPlant extends AspectHolder {
    protected PlantType type;
    protected int age;
    private int currentStage;

    protected AbstractPlant(Position position) {
        super(position);
    }

    public PlantType.PlantLifeStage getCurrentStage() {
        return type.getLifeStages().get(currentStage);
    }

    public abstract boolean isHarvestable();

    public PlantType getType() {
        return type;
    }

    public void setType(PlantType type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public abstract Position getPosition();
}
