package stonering.entity.unit.aspects.job;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.unit.SkillMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander on 27.01.2020.
 */
public class SkillAspect extends Aspect {
    public final Map<String, SkillValue> skills;

    public SkillAspect(Entity entity) {
        super(entity);
        skills = new HashMap<>();
    }

    public SkillValue getSkill(String skill) {
        return skills.computeIfAbsent(skill, SkillValue::new); // create new skill with 0 experience
    }

    /**
     * Holds current level and experience of some skill.
     */
    public static class SkillValue {
        public final String skill;
        public final LeveledValue state;

        public SkillValue(String skill) {
            this.skill = skill;
            state = new LeveledValue(SkillMap.getSkill(skill).levels);
        }
    }
}
