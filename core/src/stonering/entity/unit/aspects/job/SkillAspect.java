package stonering.entity.unit.aspects.job;

import java.util.HashMap;
import java.util.Map;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * @author Alexander on 27.01.2020.
 */
public class SkillAspect extends Aspect {
    public final Map<String, SkillState> skills;

    public SkillAspect(Entity entity) {
        super(entity);
        skills = new HashMap<>();
    }

    public SkillState getSkill(String skill) {
        return skills.computeIfAbsent(skill, SkillState::new); // create new skill with 0 experience
    }

    public static class SkillState {
        public final String skill;
        public final SkillValue state;

        public SkillState(String skill) {
            this.skill = skill;
            state = new SkillValue();
        }
    }
}
