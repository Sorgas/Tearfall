package stonering.entity.unit.aspects.job;

import static stonering.enums.unit.SkillMap.LEVEL_AMOUNT;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.MathUtils;

/**
 * Stores level and progress to next level for a skill.
 *
 * @author Alexander on 26.01.2020
 */
public class SkillValue implements Comparable<SkillValue> {
    public final String skillName;
    private int value = 0;
    private int level = 0;

    public SkillValue(String skillName) {
        this.skillName = skillName;
    }

    public float nextLevelProgress() {
        return value / (float) LEVEL_AMOUNT[level];
    }

    public void setValue(int value) {
        this.value = value;
        checkLevelChange();
    }

    public void setLevel(int level) {
        this.level = MathUtils.clamp(level, 0, LEVEL_AMOUNT.length);
        value = 0;
    }

    public void changeValue(int delta) {
        while (delta != 0) {
            int toApply = delta > 0
                    ? Math.min(delta, LEVEL_AMOUNT[level] - value) // diff to next level
                    : Math.max(delta, -value - 1); // diff to previous level
            delta -= toApply;
            value += toApply;
            checkLevelChange();
            // delta cannot be applied anymore
            if (delta < 0 && level == 0 || delta > 0 && level == LEVEL_AMOUNT.length) return;
        }
    }

    private void checkLevelChange() {
        if (value < 0) { // previous level
            level--;
            value = LEVEL_AMOUNT[level] - 1;
        } else if (value >= LEVEL_AMOUNT[level]) { // next level
            level++;
            value = 0;
        }
    }

    public float value() {
        return value;
    }

    public int level() {
        return level;
    }

    @Override
    public int compareTo(@NotNull SkillValue o) {
        return level != o.level ? level - o.level : value - o.value;
    }
}
