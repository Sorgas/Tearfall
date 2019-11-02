package stonering.entity.plants;

import stonering.entity.Entity;
import stonering.enums.plants.PlantLifeStage;
import stonering.enums.plants.PlantType;
import stonering.util.geometry.Position;

/**
 * Parent class for single and multi tile plants.
 */
public abstract class AbstractPlant extends Entity {
    protected PlantType type;
    private int age; // months
    private int currentStage;
    private boolean dead;

    protected AbstractPlant(Position position, PlantType type, int age) {
        super(position);
        this.age = age;
        this.type = type;
        countStage();
    }

    protected AbstractPlant(PlantType type, int age) {
        super();
        this.age = age;
        this.type = type;
        countStage();
    }

    /**
     * Initializes stage number by plant name;
     */
    private void countStage() {
        for (PlantLifeStage stage : type.lifeStages) {
            if(stage.stageEnd > age && (stage.stageEnd - stage.stageLength) <= age)
                currentStage = type.lifeStages.indexOf(stage);
        }
    }

    /**
     * Increases age by 1 month.
     *
     * @return 0 - stage continues,
     *         1 - stage changed,
     *         -1 - last stage ended.
     */
    public int increaceAge() {
        if (dead) return -1;
        age++;
        if (type.lifeStages.get(currentStage).getStageEnd() > age) return 0;
        currentStage++;
        return currentStage == type.lifeStages.size() ? -1 : 1;
    }

    public PlantLifeStage getCurrentStage() {
        return type.lifeStages.get(currentStage);
    }

    public abstract boolean isHarvestable();

    public int getCurrentStageIndex() {
        return currentStage;
    }

    public PlantType getType() {
        return type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
