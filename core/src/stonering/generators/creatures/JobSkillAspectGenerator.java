package stonering.generators.creatures;

import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.entity.unit.aspects.job.SkillValue;
import stonering.enums.unit.SkillMap;

/**
 * @author Alexander on 9/6/2020
 */
public class JobSkillAspectGenerator {
    public JobSkillAspect generate() {
        JobSkillAspect aspect = new JobSkillAspect();
        SkillMap.all().forEach(skill -> aspect.skills.put(skill.name, new SkillValue(skill.name)));
        return aspect;
    }
}
