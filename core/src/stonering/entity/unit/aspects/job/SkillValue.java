package stonering.entity.unit.aspects.job;

import static stonering.enums.unit.SkillMap.LEVEL_BOUNDS;

/**
 * Divides value into sequence of levels.
 * Zero level always starts with 0. When value is updated, level recounted.
 *
 * @author Alexander on 26.01.2020
 */
public class SkillValue {
    private float value;
    private int level;

    public SkillValue() {
        value = 0;
        level = 0;
    }

    public void updateLevel() {
        for (int i = 0; i < LEVEL_BOUNDS.length; i++) {
            if (LEVEL_BOUNDS[i] > value) {
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
        if (level >= LEVEL_BOUNDS.length) level = LEVEL_BOUNDS.length;
        if (level <= 0) {
            level = 0;
            value = 0;
        } else {
            value = LEVEL_BOUNDS[level - 1];
        }
    }

    public float value() {
        return value;
    }

    public int level() {
        return level;
    }
    
    public int maxLevel() {
        return LEVEL_BOUNDS.length;
    }
}
