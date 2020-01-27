package stonering.entity.unit.aspects.job;

/**
 * Divides value into sequence of levels.
 * Zero level always starts with 0.
 *
 * @author Alexander on 26.01.2020
 */
public class LeveledValue {
    private float value;
    private int level;
    private int[] levelBounds; // min value of a level, [0] is level 1

    public LeveledValue(int[] levelBounds) {
        this.levelBounds = levelBounds;
        value = 0;
        level = 0;
    }

    public void updateLevel() {
        for (int i = 0; i < levelBounds.length; i++) {
            if (levelBounds[i] > value) {
                level = i;
                break;
            }
        }
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        updateLevel();
    }

    public void changeValue(int delta) {
        value += delta;
        updateLevel();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int newLevel) {
        level = newLevel;
        if (level >= levelBounds.length) level = levelBounds.length;
        if (level <= 0) {
            level = 0;
            value = 0;
        } else {
            value = levelBounds[level - 1];
        }
    }
}
