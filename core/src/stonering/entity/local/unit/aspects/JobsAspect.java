package stonering.entity.local.unit.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores skills and job enabled for unit.
 *
 * @author Alexander Kuzyakov on 31.01.2018.
 */
public class JobsAspect extends Aspect {
    private ArrayList<String> jobs;
    private Map<String, Integer> skills; // skill name to level.

    public JobsAspect(Entity entity) {
        super(entity);
        jobs = new ArrayList<>();
        skills = new HashMap<>();
    }

    public float getSkillModifier(String skillName) {
        return 1f;
    }

    public ArrayList<String> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<String> jobs) {
        this.jobs = jobs;
    }


}
