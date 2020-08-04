package stonering.entity.unit.aspects.job;

/**
 * Divides value into sequence of levels.
 * Zero level always starts with 0. When value is updated, level recounted.
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

    public void setValue(float value) {
        this.value = value;
        updateLevel();
    }

    public void changeValue(int delta) {
        value += delta;
        updateLevel();
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

    public float value() {
        return value;
    }

    public int level() {
        return level;
    }
    
    public int maxLevel() {
        return levelBounds.length;
    }
}
