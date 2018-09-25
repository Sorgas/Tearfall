package stonering.objects.local_actors.plants;

import stonering.enums.plants.PlantType;
import stonering.global.utils.Position;

public abstract class AbstractPlant {
    protected PlantType type;
    protected int age;
    private int currentStage;

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
