package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

import java.util.*;

/**
 * Stores skills and job enabled for unit.
 * Job set determines what {@link stonering.entity.job.Task}s  can be performed by unit.
 * Skill values influence effectiveness of some {@link stonering.entity.job.action.Action}s.
 * //TODO exp/level table.
 *
 * @author Alexander Kuzyakov on 31.01.2018.
 */
public class JobsAspect extends Aspect {
    private Set<String> enabledJobs;
    private Map<String, Integer> skills; // skill name to level.
    private Map<String, Integer> experience;

    public JobsAspect(Entity entity) {
        super(entity);
        enabledJobs = new HashSet<>();
        skills = new HashMap<>();
    }

    public float getSkillModifier(String skillName) {
        return 1f;
    }

    public Set<String> getEnabledJobs() {
        return enabledJobs;
    }
}
