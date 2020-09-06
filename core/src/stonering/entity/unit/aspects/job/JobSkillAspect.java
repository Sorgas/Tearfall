package stonering.entity.unit.aspects.job;

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
public class JobSkillAspect extends Aspect {
    public Set<String> enabledJobs = new HashSet<>();
    public Map<String, SkillValue> skills = new HashMap<>(); // skill name to level.
}
