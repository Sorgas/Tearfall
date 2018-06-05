package stonering.objects.local_actors.plants;

import stonering.enums.plants.PlantType;

public abstract class AbstractPlant {
    protected PlantType type;
    protected int age;

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
}
