package stonering.entity.unit.aspects.job;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.unit.job.JobsEnum;

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
    private Set<JobsEnum> enabledJobs;
    private Map<JobsEnum, LeveledValue> skills; // skill name to level.

    public JobsAspect(Entity entity) {
        super(entity);
        enabledJobs = new HashSet<>();
        skills = new HashMap<>();
        enabledJobs.add(JobsEnum.NONE);
    }

    public Set<JobsEnum> getEnabledJobs() {
        return enabledJobs;
    }
}
